package at.szybbs.tacc.taccbackend.service.teslaConnection

import at.szybbs.tacc.taccbackend.dto.teslaConnection.TeslaConnectionCreationDto
import at.szybbs.tacc.taccbackend.exception.teslaConnection.*
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationNotFoundException
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnection
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnectionId
import at.szybbs.tacc.taccbackend.repository.teslaConnection.TeslaConnectionRepository
import at.szybbs.tacc.taccbackend.service.userInformation.UserInformationService
import at.szybbs.tacc.taccbackend.validation.teslaConnection.TeslaConnectionUserInputConfig
import at.szybbs.tacc.taccbackend.validation.teslaConnection.TeslaConnectionUserInputConfigValidatorMapper
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class TeslaConnectionService (
    private val teslaConnectionRepository: TeslaConnectionRepository,
    private val userInformationService: UserInformationService,
    private val teslaConnectionHelperService: TeslaConnectionHelperService,
    private val userInputConfigValidator: TeslaConnectionUserInputConfigValidatorMapper
) {
    /**
     * Returns all TeslaConnections for a specified user. Ensures only one or zero active connection exists by
     * deactivating all, if multiple are found.
     * @param userInformationId UUID of the user
     * @return List of TeslaConnection objects for the user
     * @throws UserInformationNotFoundException if no information for the user exists
     */
    fun getAllTeslaConnections(userInformationId: UUID): List<TeslaConnection> {
        if (!userInformationService.userInformationExists(userInformationId)) throw UserInformationNotFoundException(userInformationId)

        var teslaConnections = teslaConnectionRepository.findByUserInformationId(userInformationId)

        // check if there is more than one active TeslaConnection of a user
        if (teslaConnections.filter { it.active }.size > 1) {
            teslaConnectionHelperService.setAllTeslaConnectionsToInactive(userInformationId)

            // Re-fetch the data to ensure we have the most up-to-date state
            teslaConnections = teslaConnectionRepository.findByUserInformationId(userInformationId)
        }

        return teslaConnections
    }

    /**
     * Returns a TeslaConnection of a specific type a user by the ID provided.
     * No checks are performed to ensure there is only one active TeslaConnection.
     * @param teslaConnectionId Unique identifier for the TeslaConnection
     * @return The TeslaConnection with the specified ID
     * @throws TeslaConnectionNotFoundException if no TeslaConnection with the specified ID is found
     */
    fun getTeslaConnection(teslaConnectionId: TeslaConnectionId): TeslaConnection {
        return teslaConnectionRepository.findById(teslaConnectionId)
            .orElseThrow { TeslaConnectionNotFoundException(teslaConnectionId) }
    }

    /**
     * Returns the active TeslaConnection for a specified user, ensuring only one is active.
     * Sets all connections to inactive, if multiple active connections are detected.
     * @param userInformationId UUID of the user
     * @return The active TeslaConnection for the user
     * @throws UserInformationNotFoundException if no information for the user exists
     * @throws ActiveTeslaConnectionNotFoundException if no active connection is found (sometimes caused by multiple active TeslaConnections)
     */
    fun getActiveTeslaConnection(userInformationId: UUID): TeslaConnection {
        if (!userInformationService.userInformationExists(userInformationId)) throw UserInformationNotFoundException(userInformationId)

        // list if all active TeslaConnections of a user, should only contain one item
        val activeTeslaConnection = teslaConnectionRepository.findByUserInformationIdAndActiveIsTrue(userInformationId)

        // if there's more than one active TeslaConnection, all active ones are set to inactive
        if (activeTeslaConnection.size > 1) {
            teslaConnectionHelperService.setAllTeslaConnectionsToInactive(userInformationId)
            throw ActiveTeslaConnectionNotFoundException(userInformationId)
        }

        // return the active TeslaConnection (first one from list), if list is empty throw ActiveTeslaConnectionNotFoundException
        return activeTeslaConnection.firstOrNull() ?: throw ActiveTeslaConnectionNotFoundException(userInformationId)
    }

    /**
     * Creates a new TeslaConnection with the data provided in a TeslaConnectionCreationDto.
     * Validates and saves the new connection, optionally setting it to active if specified.
     * The config of the connection will be handled as if it was from a user input, so only fields specified in [TeslaConnectionUserInputConfig] for each type are allowed.
     * @param teslaConnectionId Unique identifier for the new TeslaConnection
     * @param teslaConnectionCreationDto DTO containing details of the new connection
     * @return The newly created TeslaConnection, activated if specified
     * @throws TeslaConnectionAlreadyExistsException if a connection with the ID already exists
     * @throws UserInformationNotFoundException if no information for the user exists
     * @throws UnsupportedTeslaConnectionTypeException if the TeslaConnectionType is not supported
     * @throws InvalidTeslaConnectionConfigFormatException if the configuration format is invalid
     * @throws IllegalTeslaConnectionConfigValueException if configuration values are illegal
     */
    @Transactional
    fun createTeslaConnectionFromUserInput(
        teslaConnectionId: TeslaConnectionId,
        teslaConnectionCreationDto: TeslaConnectionCreationDto
    ): TeslaConnection {
        if (teslaConnectionExists(teslaConnectionId)) throw TeslaConnectionAlreadyExistsException(teslaConnectionId)

        if (!userInformationService.userInformationExists(teslaConnectionId.userInformationId)) throw UserInformationNotFoundException(teslaConnectionId.userInformationId)

        // Validate configuration based on TeslaConnectionType and config input
        val validatedConfig = userInputConfigValidator.validateAndMap(
            teslaConnectionType = teslaConnectionId.type,
            config = teslaConnectionCreationDto.config,
        )

        // Save new inactive TeslaConnection
        teslaConnectionRepository.save(
            TeslaConnection(
            id = teslaConnectionId,
            active = false,
            config = validatedConfig
        )
        )

        // If not intended to be active, return as saved; otherwise, activate
        if (!teslaConnectionCreationDto.active) {
            return getTeslaConnection(teslaConnectionId)
        }

        return setTeslaConnectionToActive(teslaConnectionId)
    }

    /**
     * Sets the specified TeslaConnection to active, deactivating all other active connections for the user.
     * @param teslaConnectionId Unique identifier for the TeslaConnection to set active
     * @return The activated TeslaConnection
     * @throws TeslaConnectionNotFoundException if no connection with the specified ID is found
     */
    @Transactional
    fun setTeslaConnectionToActive(teslaConnectionId: TeslaConnectionId): TeslaConnection {
        // get the TeslaConnection of a user which should be set to active
        val toActiveTeslaConnectionOpt = teslaConnectionRepository.findById(teslaConnectionId)

        if (toActiveTeslaConnectionOpt.isEmpty) throw TeslaConnectionNotFoundException(teslaConnectionId)

        val toActiveTeslaConnection = toActiveTeslaConnectionOpt.get()

        // deactivate all active teslaConnections
        teslaConnectionHelperService.setAllTeslaConnectionsToInactive(teslaConnectionId.userInformationId)

        // set the TeslaConnection of a user, which should be set to active, active
        toActiveTeslaConnection.active = true

        return toActiveTeslaConnection
    }

    /**
     * Deactivates the specified TeslaConnection and returns it.
     * @param teslaConnectionId Unique identifier for the TeslaConnection to deactivate
     * @return The deactivated TeslaConnection
     * @throws TeslaConnectionNotFoundException if no connection with the specified ID is found
     */
    @Transactional
    fun setTeslaConnectionToInactive(teslaConnectionId: TeslaConnectionId): TeslaConnection {
        // get the TeslaConnection of a user which should be set to inactive
        val toInactiveTeslaConnectionOpt = teslaConnectionRepository.findById(teslaConnectionId)

        if (toInactiveTeslaConnectionOpt.isEmpty) throw TeslaConnectionNotFoundException(teslaConnectionId)

        val toInactiveTeslaConnection = toInactiveTeslaConnectionOpt.get()

        // set the TeslaConnection of a user, which should be set to inactive, inactive
        toInactiveTeslaConnection.active = false

        return toInactiveTeslaConnection
    }

    /**
     * Updates the configuration (only the fields a user is allowed to change) of an existing TeslaConnection with a new configuration.
     * Not existing fields in the configuration are created and existing ones are overwritten.
     * Only fields specified in [TeslaConnectionUserInputConfig] for each type are allowed.
     * @param teslaConnectionId Unique identifier for the TeslaConnection to update
     * @param newConfig New configuration as a JsonNode
     * @return The updated TeslaConnection
     * @throws TeslaConnectionNotFoundException if no connection with the specified ID is found
     * @throws UnsupportedTeslaConnectionTypeException if the TeslaConnectionType is unsupported
     * @throws InvalidTeslaConnectionConfigFormatException if the configuration format is invalid
     * @throws IllegalTeslaConnectionConfigValueException if configuration values are illegal
     */
    @Transactional
    fun updateTeslaConnectionConfigFromUserInput(
        teslaConnectionId: TeslaConnectionId,
        newConfig: JsonNode,
    ): TeslaConnection {
        val teslaConnectionOpt = teslaConnectionRepository.findById(teslaConnectionId)

        if (teslaConnectionOpt.isEmpty) throw TeslaConnectionNotFoundException(teslaConnectionId)

        val teslaConnection = teslaConnectionOpt.get()

        // Validate and map the new config according to the TeslaConnectionType requirements
        val validatedConfig = userInputConfigValidator.validateAndMap(
            teslaConnectionType = teslaConnectionId.type,
            config = newConfig
        )

        // Cast current config to ObjectNode to allow updating specific fields
        val existingConfig = teslaConnection.config as ObjectNode

        // Merge validated fields into existingConfig
        existingConfig.setAll<JsonNode>(validatedConfig as ObjectNode)

        // Save the updated configuration back to the TeslaConnection object
        teslaConnection.config = existingConfig

        return teslaConnection
    }

    /**
     * Deletes the specified TeslaConnection for the provided ID.
     * @param teslaConnectionId Unique identifier for the TeslaConnection to delete
     * @throws TeslaConnectionNotFoundException if no connection with the specified ID is found
     */
    fun deleteTeslaConnection(teslaConnectionId: TeslaConnectionId) {
        val teslaConnectionOpt = teslaConnectionRepository.findById(teslaConnectionId)

        if (teslaConnectionOpt.isEmpty) throw TeslaConnectionNotFoundException(teslaConnectionId)

        teslaConnectionRepository.delete(teslaConnectionOpt.get())
    }

    /**
     * Checks if a TeslaConnection with the specified ID exists in the repository.
     * @param teslaConnectionId Unique identifier for the TeslaConnection to check
     * @return Boolean indicating if the TeslaConnection exists
     */
    fun teslaConnectionExists(teslaConnectionId: TeslaConnectionId): Boolean {
        return teslaConnectionRepository.findById(teslaConnectionId).isPresent
    }
}