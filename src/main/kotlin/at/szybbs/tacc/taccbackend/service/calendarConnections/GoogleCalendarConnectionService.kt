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
import at.szybbs.tacc.taccbackend.repository.calendarConnections.GoogleCalendarConnectionRepository
import at.szybbs.tacc.taccbackend.service.userInformation.UserInformationService
import org.springframework.stereotype.Service
import java.util.*

@Service
class GoogleCalendarConnectionService (
    private val googleCalendarConnectionRepository: GoogleCalendarConnectionRepository,
    private val userInformationService: UserInformationService,
) {

    @Throws(CalendarConnectionNotFoundException::class)
    fun getCalendarConnection(userInformationId: UUID): GoogleCalendarConnection {
       return googleCalendarConnectionRepository.findById(userInformationId)
           .orElseThrow { CalendarConnectionNotFoundException(CalendarType.GOOGLE_CALENDAR, userInformationId) }
    }

    @Throws(
        UserInformationNotFoundException::class,
        CalendarConnectionAlreadyExistsException::class,
        CalendarConnectionValidationException::class,
    )
    fun createCalendarConnection(
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
    fun updateCalendarConnectionPublicFields(
        userInformationId: UUID,
        updateDto: GoogleCalendarConnectionUpdateDto
    ): GoogleCalendarConnection? {
        val calendarConnection = getCalendarConnection(userInformationId)

        if (!updateDto.hasChanged(calendarConnection)) return null

        calendarConnection.keyword = updateDto.keyword

        val updatedCalendarConnection = googleCalendarConnectionRepository.save(calendarConnection)

        // TODO: call/update http-Client

        return updatedCalendarConnection
    }

    @Throws(CalendarConnectionNotFoundException::class)
    fun deleteCalendarConnection(userInformationId: UUID) {
        val googleCalendarConnection = getCalendarConnection(userInformationId)

        googleCalendarConnectionRepository.delete(googleCalendarConnection)
    }

    @Throws(
        UserInformationNotFoundException::class,
        CalendarConnectionNotFoundException::class,
    )
    fun setCalendarConnectionToActive(userInformationId: UUID): UserInformation? {
        if (!calendarConnectionExists(userInformationId))
            throw CalendarConnectionNotFoundException(CalendarType.GOOGLE_CALENDAR, userInformationId)

        return userInformationService.setActiveCalendarConnectionType(userInformationId, CalendarType.GOOGLE_CALENDAR)
    }

    fun calendarConnectionExists(userInformationId: UUID): Boolean {
        return googleCalendarConnectionRepository.findById(userInformationId).isPresent
    }
}