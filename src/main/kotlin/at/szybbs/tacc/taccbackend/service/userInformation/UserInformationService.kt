package at.szybbs.tacc.taccbackend.service.userInformation


import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationCreationDto
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationUpdateDefaultValuesDto
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationAlreadyExistsException
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationNotFoundException
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationValidationException
import at.szybbs.tacc.taccbackend.repository.userInformation.UserInformationRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserInformationService (
    private val userInformationRepository: UserInformationRepository
) {

    fun getUserInformation() : List<UserInformation> {
        return userInformationRepository.findAll()
    }

    @Throws(UserInformationNotFoundException::class)
    fun getUserInformation(userInformationId: UUID) : UserInformation {
        return userInformationRepository.findById(userInformationId)
            .orElseThrow { UserInformationNotFoundException(userInformationId) }
    }

    @Throws(
        UserInformationAlreadyExistsException::class,
        UserInformationValidationException::class,
    )
    fun createUserInformation(userInformationId: UUID, creationDto: UserInformationCreationDto, jwtEmail: String) : UserInformation {
        if (userInformationExists(userInformationId)) throw UserInformationAlreadyExistsException(userInformationId)

        val newUserInformation = UserInformation(
            id = userInformationId,
            email = jwtEmail,
            noDestMinutes = creationDto.noDestMinutes,
            ccRuntimeMinutes = creationDto.ccRuntimeMinutes,
            arrivalBufferMinutes = creationDto.arrivalBufferMinutes,
        )

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
    fun updateUserInformationDefaultValues(userInformationId: UUID, updateDto: UserInformationUpdateDefaultValuesDto) : UserInformation {
        val userInformation = getUserInformation(userInformationId)

        userInformation.noDestMinutes = updateDto.noDestMinutes
        userInformation.ccRuntimeMinutes = updateDto.ccRuntimeMinutes
        userInformation.arrivalBufferMinutes = updateDto.arrivalBufferMinutes

        return userInformationRepository.save(userInformation)
    }

    @Throws(UserInformationNotFoundException::class)
    fun setActiveCalendarConnectionType(userInformationId: UUID, calendarType: CalendarType?): UserInformation {
        val userInformation = getUserInformation(userInformationId)

        userInformation.activeCalendarConnectionType = calendarType

        return userInformationRepository.save(userInformation)
    }

    @Throws(UserInformationNotFoundException::class)
    fun setActiveTeslaConnectionType(userInformationId: UUID, teslaConnectionType: TeslaConnectionType?) : UserInformation {
        val userInformation = getUserInformation(userInformationId)

        userInformation.activeTeslaConnectionType = teslaConnectionType

        return userInformationRepository.save(userInformation)
    }

    fun userInformationExists(userInformationId: UUID): Boolean {
        return userInformationRepository.findById(userInformationId).isPresent
    }

    fun setOauth2Session(userInformationId: UUID, oauth2Session: String) : UserInformation {
        val userInformation = getUserInformation(userInformationId)

        userInformation.oauth2Session = oauth2Session

        return userInformationRepository.save(userInformation)
    }

    fun getUserIdBySession(oauth2Session: String) : UUID? {
        return userInformationRepository.findUserInformationByOauth2Session(oauth2Session)?.id
    }
}