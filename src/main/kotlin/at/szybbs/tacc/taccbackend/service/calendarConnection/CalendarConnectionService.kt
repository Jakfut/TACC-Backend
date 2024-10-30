package at.szybbs.tacc.taccbackend.service.calendarConnection

import at.szybbs.tacc.taccbackend.dto.calendarConnection.CalendarConnectionCreationDto
import at.szybbs.tacc.taccbackend.exception.calendarConnection.ActiveCalendarConnectionNotFoundException
import at.szybbs.tacc.taccbackend.exception.calendarConnection.CalendarConnectionAlreadyExistsException
import at.szybbs.tacc.taccbackend.exception.calendarConnection.CalendarConnectionNotFoundException
import at.szybbs.tacc.taccbackend.exception.calendarConnection.UnsupportedCalendarTypeException
import at.szybbs.tacc.taccbackend.exception.calendarConnection.InvalidCalendarConnectionConfigFormatException
import at.szybbs.tacc.taccbackend.exception.calendarConnection.IllegalCalendarConnectionConfigValueException
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationNotFoundException
import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarConnection
import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarConnectionId
import at.szybbs.tacc.taccbackend.repository.calendarConnection.CalendarConnectionRepository
import at.szybbs.tacc.taccbackend.service.userInformation.UserInformationService
import at.szybbs.tacc.taccbackend.validation.calendarConnection.CalendarConnectionUserInputConfigValidatorMapper
import at.szybbs.tacc.taccbackend.validation.calendarConnection.CalendarConnectionUserInputConfig
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class CalendarConnectionService (
    private val calendarConnectionRepository: CalendarConnectionRepository,
    private val userInformationService: UserInformationService,
    private val calendarConnectionHelperService: CalendarConnectionHelperService,
    private val userInputConfigValidator: CalendarConnectionUserInputConfigValidatorMapper
) {

    /**
     * Returns all CalendarConnections for a specified user. Ensures only one or zero active connection exists by
     * deactivating all, if multiple are found.
     * @param userInformationId UUID of the user
     * @return List of CalendarConnection objects for the user
     * @throws UserInformationNotFoundException if no information for the user exists
     */
    fun getAllCalendarConnections(userInformationId: UUID): List<CalendarConnection> {
        if (!userInformationService.userInformationExists(userInformationId)) throw UserInformationNotFoundException(userInformationId)

        var calendarConnections = calendarConnectionRepository.findByUserInformationId(userInformationId)

        // check if there is more than one active CalendarConnection of a user
        if (calendarConnections.filter { it.active }.size > 1) {
            calendarConnectionHelperService.setAllCalendarConnectionsToInactive(userInformationId)

            // Re-fetch the data to ensure we have the most up-to-date state
            calendarConnections = calendarConnectionRepository.findByUserInformationId(userInformationId)
        }

        return calendarConnections
    }

    /**
     * Returns a CalendarConnection of a specific type a user by the ID provided.
     * No checks are performed to ensure there is only one active CalendarConnection.
     * @param calendarConnectionId Unique identifier for the CalendarConnection
     * @return The CalendarConnection with the specified ID
     * @throws CalendarConnectionNotFoundException if no CalendarConnection with the specified ID is found
     */
    fun getCalendarConnection(calendarConnectionId: CalendarConnectionId): CalendarConnection {
        return calendarConnectionRepository.findById(calendarConnectionId)
            .orElseThrow { CalendarConnectionNotFoundException(calendarConnectionId) }
    }

    /**
     * Returns the active CalendarConnection for a specified user, ensuring only one is active.
     * Sets all connections to inactive, if multiple active connections are detected.
     * @param userInformationId UUID of the user
     * @return The active CalendarConnection for the user
     * @throws UserInformationNotFoundException if no information for the user exists
     * @throws ActiveCalendarConnectionNotFoundException if no active connection is found (sometimes caused by multiple active CalendarConnections)
     */
    fun getActiveCalendarConnection(userInformationId: UUID): CalendarConnection {
        if (!userInformationService.userInformationExists(userInformationId)) throw UserInformationNotFoundException(userInformationId)

        // list if all active CalendarConnections of a user, should only contain one item
        val activeCalendarConnection = calendarConnectionRepository.findByUserInformationIdAndActiveIsTrue(userInformationId)

        // if there's more than one active CalendarConnection, all active ones are set to inactive
        if (activeCalendarConnection.size > 1) {
            calendarConnectionHelperService.setAllCalendarConnectionsToInactive(userInformationId)
            throw ActiveCalendarConnectionNotFoundException(userInformationId)
        }

        // return the active CalendarConnection (first one from list), if list is empty throw ActiveCalendarConnectionNotFoundException
        return activeCalendarConnection.firstOrNull() ?: throw ActiveCalendarConnectionNotFoundException(userInformationId)
    }

    /**
     * Creates a new CalendarConnection with the data provided in a CalendarConnectionCreationDto.
     * Validates and saves the new connection, optionally setting it to active if specified.
     * The config of the connection will be handled as if it was from a user input, so only fields specified in [CalendarConnectionUserInputConfig] for each type are allowed.
     * @param calendarConnectionId Unique identifier for the new CalendarConnection
     * @param calendarConnectionCreationDto DTO containing details of the new connection
     * @return The newly created CalendarConnection, activated if specified
     * @throws CalendarConnectionAlreadyExistsException if a connection with the ID already exists
     * @throws UserInformationNotFoundException if no information for the user exists
     * @throws UnsupportedCalendarTypeException if the CalendarType is not supported
     * @throws InvalidCalendarConnectionConfigFormatException if the configuration format is invalid
     * @throws IllegalCalendarConnectionConfigValueException if configuration values are illegal
     */
    @Transactional
    fun createCalendarConnectionFromUserInput(
        calendarConnectionId: CalendarConnectionId,
        calendarConnectionCreationDto: CalendarConnectionCreationDto
    ): CalendarConnection {
        if (calendarConnectionExists(calendarConnectionId)) throw CalendarConnectionAlreadyExistsException(calendarConnectionId)

        if (!userInformationService.userInformationExists(calendarConnectionId.userInformationId)) throw UserInformationNotFoundException(calendarConnectionId.userInformationId)

        // Validate configuration based on CalendarType and config input
        val validatedConfig = userInputConfigValidator.validateAndMap(
            calendarType = calendarConnectionId.type,
            config = calendarConnectionCreationDto.config,
        )

        // Save new inactive CalendarConnection
        calendarConnectionRepository.save(CalendarConnection(
            id = calendarConnectionId,
            active = false,
            config = validatedConfig
        ))

        // If not intended to be active, return as saved; otherwise, activate
        if (!calendarConnectionCreationDto.active) {
            return getCalendarConnection(calendarConnectionId)
        }

        return setCalendarConnectionToActive(calendarConnectionId)
    }

    /**
     * Sets the specified CalendarConnection to active, deactivating all other active connections for the user.
     * @param calendarConnectionId Unique identifier for the CalendarConnection to set active
     * @return The activated CalendarConnection
     * @throws CalendarConnectionNotFoundException if no connection with the specified ID is found
     */
    @Transactional
    fun setCalendarConnectionToActive(calendarConnectionId: CalendarConnectionId): CalendarConnection {
        // get the CalendarConnection of a user which should be set to active
        val toActiveCalendarConnectionOpt = calendarConnectionRepository.findById(calendarConnectionId)

        if (toActiveCalendarConnectionOpt.isEmpty) throw CalendarConnectionNotFoundException(calendarConnectionId)

        val toActiveCalendarConnection = toActiveCalendarConnectionOpt.get()

        // deactivate all active calendarConnections
        calendarConnectionHelperService.setAllCalendarConnectionsToInactive(calendarConnectionId.userInformationId)

        // set the CalendarConnection of a user, which should be set to active, active
        toActiveCalendarConnection.active = true

        return toActiveCalendarConnection
    }

    /**
     * Deactivates the specified CalendarConnection and returns it.
     * @param calendarConnectionId Unique identifier for the CalendarConnection to deactivate
     * @return The deactivated CalendarConnection
     * @throws CalendarConnectionNotFoundException if no connection with the specified ID is found
     */
    @Transactional
    fun setCalendarConnectionToInactive(calendarConnectionId: CalendarConnectionId): CalendarConnection {
        // get the CalendarConnection of a user which should be set to inactive
        val toInactiveCalendarConnectionOpt = calendarConnectionRepository.findById(calendarConnectionId)

        if (toInactiveCalendarConnectionOpt.isEmpty) throw CalendarConnectionNotFoundException(calendarConnectionId)

        val toInactiveCalendarConnection = toInactiveCalendarConnectionOpt.get()

        // set the CalendarConnection of a user, which should be set to inactive, inactive
        toInactiveCalendarConnection.active = false

        return toInactiveCalendarConnection
    }

    /**
     * Updates the configuration of an existing CalendarConnection with a new configuration.
     * Not existing fields in the configuration are created and existing ones are overwritten.
     * Only fields specified in [CalendarConnectionUserInputConfig] for each type are allowed.
     * @param calendarConnectionId Unique identifier for the CalendarConnection to update
     * @param newConfig New configuration as a JsonNode
     * @return The updated CalendarConnection
     * @throws CalendarConnectionNotFoundException if no connection with the specified ID is found
     * @throws UnsupportedCalendarTypeException if the CalendarType is unsupported
     * @throws InvalidCalendarConnectionConfigFormatException if the configuration format is invalid
     * @throws IllegalCalendarConnectionConfigValueException if configuration values are illegal
     */
    @Transactional
    fun updateCalendarConnectionConfigFromUserInput(
        calendarConnectionId: CalendarConnectionId,
        newConfig: JsonNode,
    ): CalendarConnection {
        val calendarConnectionOpt = calendarConnectionRepository.findById(calendarConnectionId)

        if (calendarConnectionOpt.isEmpty) throw CalendarConnectionNotFoundException(calendarConnectionId)

        val calendarConnection = calendarConnectionOpt.get()

        // Validate and map the new config according to the CalendarType requirements
        val validatedConfig = userInputConfigValidator.validateAndMap(
            calendarType = calendarConnectionId.type,
            config = newConfig
        )

        // Cast current config to ObjectNode to allow updating specific fields
        val existingConfig = calendarConnection.config as ObjectNode

        // Merge validated fields into existingConfig
        existingConfig.setAll<JsonNode>(validatedConfig as ObjectNode)

        // Save the updated configuration back to the CalendarConnection object
        calendarConnection.config = existingConfig

        return calendarConnection
    }

    /**
     * Deletes the specified CalendarConnection for the provided ID.
     * @param calendarConnectionId Unique identifier for the CalendarConnection to delete
     * @throws CalendarConnectionNotFoundException if no connection with the specified ID is found
     */
    fun deleteCalendarConnection(calendarConnectionId: CalendarConnectionId) {
        val calendarConnectionOpt = calendarConnectionRepository.findById(calendarConnectionId)

        if (calendarConnectionOpt.isEmpty) throw CalendarConnectionNotFoundException(calendarConnectionId)

        calendarConnectionRepository.delete(calendarConnectionOpt.get())
    }

    /**
     * Checks if a CalendarConnection with the specified ID exists in the repository.
     * @param calendarConnectionId Unique identifier for the CalendarConnection to check
     * @return Boolean indicating if the CalendarConnection exists
     */
    fun calendarConnectionExists(calendarConnectionId: CalendarConnectionId): Boolean {
        return calendarConnectionRepository.findById(calendarConnectionId).isPresent
    }

    // TODO: check connection on every update or add extra checkConnection function?
}