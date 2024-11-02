package at.szybbs.tacc.taccbackend.service.calendarConnections

import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionUpdateDto
import at.szybbs.tacc.taccbackend.exception.calendarConnections.CalendarConnectionAlreadyExistsException
import at.szybbs.tacc.taccbackend.exception.calendarConnections.CalendarConnectionNotFoundException
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationNotFoundException
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar.GoogleCalendarConnection
import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import at.szybbs.tacc.taccbackend.exception.calendarConnections.CalendarConnectionValidationException
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationCalendarConnectionAlreadyActiveException
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationUnexpectedStateException
import at.szybbs.tacc.taccbackend.repository.calendarConnections.GoogleCalendarConnectionRepository
import at.szybbs.tacc.taccbackend.service.userInformation.UserInformationService
import org.springframework.stereotype.Service
import java.util.*

@Service
class GoogleCalendarConnectionService (
    private val googleCalendarConnectionRepository: GoogleCalendarConnectionRepository,
    private val userInformationService: UserInformationService,
) : CalendarConnectionService<
        GoogleCalendarConnection,
        GoogleCalendarConnectionCreationDto,
        GoogleCalendarConnectionUpdateDto> {

    @Throws(CalendarConnectionNotFoundException::class)
    override fun getCalendarConnection(userInformationId: UUID): GoogleCalendarConnection {
       return googleCalendarConnectionRepository.findById(userInformationId)
           .orElseThrow { CalendarConnectionNotFoundException(CalendarType.GOOGLE_CALENDAR, userInformationId) }
    }

    @Throws(
        UserInformationNotFoundException::class,
        CalendarConnectionAlreadyExistsException::class,
        CalendarConnectionValidationException::class,
    )
    override fun createCalendarConnection(
        userInformationId: UUID,
        creationDto: GoogleCalendarConnectionCreationDto
    ): GoogleCalendarConnection {
        if (!userInformationService.userInformationExists(userInformationId)) throw UserInformationNotFoundException(userInformationId)

        if (calendarConnectionExists(userInformationId)) throw CalendarConnectionAlreadyExistsException(CalendarType.GOOGLE_CALENDAR, userInformationId)

        val newGoogleCalendarConnection = GoogleCalendarConnection(
            userInformationId = userInformationId,
            keyword = creationDto.keyword,
        )

        return googleCalendarConnectionRepository.save(newGoogleCalendarConnection)
    }

    @Throws(
        CalendarConnectionNotFoundException::class,
        CalendarConnectionValidationException::class,
    )
    override fun updateCalendarConnectionPublicFields(
        userInformationId: UUID,
        updateDto: GoogleCalendarConnectionUpdateDto
    ): GoogleCalendarConnection {
        val updatedGoogleCalendarConnection = getCalendarConnection(userInformationId)

        updatedGoogleCalendarConnection.keyword = updateDto.keyword

        return googleCalendarConnectionRepository.save(updatedGoogleCalendarConnection)
    }

    @Throws(CalendarConnectionNotFoundException::class)
    override fun deleteCalendarConnection(userInformationId: UUID) {
        val googleCalendarConnection = getCalendarConnection(userInformationId)

        googleCalendarConnectionRepository.delete(googleCalendarConnection)
    }

    @Throws(
        UserInformationNotFoundException::class,
        CalendarConnectionNotFoundException::class,
        UserInformationCalendarConnectionAlreadyActiveException::class,
    )
    override fun setCalendarConnectionToActive(userInformationId: UUID): UserInformation {
        val userInformation = userInformationService.getUserInformation(userInformationId)

        if (userInformation.activeCalendarConnectionType == CalendarType.GOOGLE_CALENDAR)
            throw UserInformationCalendarConnectionAlreadyActiveException(userInformationId, CalendarType.GOOGLE_CALENDAR)

        if (!calendarConnectionExists(userInformationId))
            throw CalendarConnectionNotFoundException(CalendarType.GOOGLE_CALENDAR, userInformationId)

        return userInformationService.setActiveCalendarConnectionType(userInformationId, CalendarType.GOOGLE_CALENDAR)
    }

    @Throws(
        UserInformationNotFoundException::class,
        CalendarConnectionNotFoundException::class,
        UserInformationUnexpectedStateException::class,
    )
    override fun setCalendarConnectionToInactive(userInformationId: UUID): UserInformation {
        val userInformation = userInformationService.getUserInformation(userInformationId)

        if (userInformation.activeCalendarConnectionType != CalendarType.GOOGLE_CALENDAR)
            throw UserInformationUnexpectedStateException("activeCalendarConnectionType", CalendarType.GOOGLE_CALENDAR.name)

        if (!calendarConnectionExists(userInformationId))
            throw CalendarConnectionNotFoundException(CalendarType.GOOGLE_CALENDAR, userInformationId)

        return userInformationService.setActiveCalendarConnectionType(userInformationId, null)
    }

    override fun calendarConnectionExists(userInformationId: UUID): Boolean {
        return googleCalendarConnectionRepository.findById(userInformationId).isPresent
    }
}