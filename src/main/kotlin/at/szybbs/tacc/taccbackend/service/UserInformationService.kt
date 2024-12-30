package at.szybbs.tacc.taccbackend.service


import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationCreationDto
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationUpdateDefaultValuesDto
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarConnection
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnection
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import at.szybbs.tacc.taccbackend.exception.calendarConnections.CalendarConnectionNotFoundException
import at.szybbs.tacc.taccbackend.exception.teslaConnections.TeslaConnectionNotFoundException
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationAlreadyExistsException
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationNotFoundException
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationValidationException
import at.szybbs.tacc.taccbackend.repository.UserInformationRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserInformationService (
    private val userInformationRepository: UserInformationRepository
) {

    @Throws(UserInformationNotFoundException::class)
    fun getUserInformation(userInformationId: UUID) : UserInformation {
        return userInformationRepository.findById(userInformationId)
            .orElseThrow { UserInformationNotFoundException(userInformationId) }
    }

    @Throws(
        UserInformationAlreadyExistsException::class,
        UserInformationValidationException::class,
    )
    fun createUserInformation(userInformationId: UUID, creationDto: UserInformationCreationDto?, jwtEmail: String) : UserInformation {
        if (userInformationExists(userInformationId)) throw UserInformationAlreadyExistsException(userInformationId)

        val newUserInformation = if (creationDto != null) {
            UserInformation(
                id = userInformationId,
                email = jwtEmail,
                noDestMinutes = creationDto.noDestMinutes,
                ccRuntimeMinutes = creationDto.ccRuntimeMinutes,
                arrivalBufferMinutes = creationDto.arrivalBufferMinutes,
            )
        } else {
            UserInformation(
                id = userInformationId,
                email = jwtEmail,
            )
        }

        return userInformationRepository.save(newUserInformation)
    }

    @Throws(UserInformationNotFoundException::class)
    fun deleteUserInformation(userInformationId: UUID) {
        val userInformation = getUserInformation(userInformationId)

        userInformationRepository.delete(userInformation)
    }

    @Throws(
        UserInformationNotFoundException::class,
        UserInformationValidationException::class,
    )
    fun updateUserInformationDefaultValues(userInformationId: UUID, updateDto: UserInformationUpdateDefaultValuesDto) : UserInformation? {
        val userInformation = getUserInformation(userInformationId)

        if (!updateDto.hasChanged(userInformation)) return null

        userInformation.noDestMinutes = updateDto.noDestMinutes
        userInformation.ccRuntimeMinutes = updateDto.ccRuntimeMinutes
        userInformation.arrivalBufferMinutes = updateDto.arrivalBufferMinutes

        val updatedUserInformation = userInformationRepository.save(userInformation)

        // TODO: call/update http-Client

        return updatedUserInformation
    }

    @Throws(
        UserInformationNotFoundException::class,
        CalendarConnectionNotFoundException::class,
    )
    fun <T : CalendarConnection>setActiveCalendarConnectionType(userInformationId: UUID, calendarType: CalendarType, repo: JpaRepository<T, UUID>): UserInformation? {
        val userInformation = getUserInformation(userInformationId)

        repo.findById(userInformationId).orElseThrow { CalendarConnectionNotFoundException(calendarType, userInformationId) }

        if (userInformation.activeCalendarConnectionType == calendarType) return null

        userInformation.activeCalendarConnectionType = calendarType

        val updatedUserInformation = userInformationRepository.save(userInformation)

        // TODO: call/update http-Client -> maybe check if connection can be established to throw an error if it's not possible

        return updatedUserInformation
    }

    @Throws(
        UserInformationNotFoundException::class,
    )
    fun setActiveCalendarConnectionTypeToNull(userInformationId: UUID): UserInformation? {
        val userInformation = getUserInformation(userInformationId)

        if (userInformation.activeCalendarConnectionType == null) return null

        userInformation.activeCalendarConnectionType = null

        val updatedUserInformation = userInformationRepository.save(userInformation)

        // TODO: call/update http-Client -> maybe check if connection can be established to throw an error if it's not possible

        return updatedUserInformation
    }

    @Throws(
        UserInformationNotFoundException::class,
        TeslaConnectionNotFoundException::class,
    )
    fun <T : TeslaConnection>setActiveTeslaConnectionType(userInformationId: UUID, teslaConnectionType: TeslaConnectionType, repo: JpaRepository<T, UUID>) : UserInformation? {
        val userInformation = getUserInformation(userInformationId)

        repo.findById(userInformationId).orElseThrow { TeslaConnectionNotFoundException(teslaConnectionType, userInformationId) }

        if (userInformation.activeTeslaConnectionType == teslaConnectionType) return null

        userInformation.activeTeslaConnectionType = teslaConnectionType

        val updatedUserInformation = userInformationRepository.save(userInformation)

        // TODO: call/update http-Client -> maybe check if connection can be established to throw an error if it's not possible

        return updatedUserInformation
    }

    @Throws(UserInformationNotFoundException::class)
    fun setActiveTeslaConnectionTypeToNull(userInformationId: UUID) : UserInformation? {
        val userInformation = getUserInformation(userInformationId)

        if (userInformation.activeTeslaConnectionType == null) return null

        userInformation.activeTeslaConnectionType = null

        val updatedUserInformation = userInformationRepository.save(userInformation)

        // TODO: call/update http-Client -> maybe check if connection can be established to throw an error if it's not possible

        return updatedUserInformation
    }

    fun userInformationExists(userInformationId: UUID): Boolean {
        return userInformationRepository.findById(userInformationId).isPresent
    }
}