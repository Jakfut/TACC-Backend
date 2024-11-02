package at.szybbs.tacc.taccbackend.service.calendarConnections

import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionUpdateDto
import at.szybbs.tacc.taccbackend.exception.calendarConnections.CalendarConnectionAlreadyExistsException
import at.szybbs.tacc.taccbackend.exception.calendarConnections.CalendarConnectionNotFoundException
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationNotFoundException
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar.GoogleCalendarConnection
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

    @Throws(CalendarConnectionNotFoundException::class,)
    override fun updateCalendarConnection(
        userInformationId: UUID,
        updateDto: GoogleCalendarConnectionUpdateDto
    ): GoogleCalendarConnection {
        val updatedGoogleCalendarConnectionOpt = googleCalendarConnectionRepository.findById(userInformationId)

        if (updatedGoogleCalendarConnectionOpt.isEmpty) throw CalendarConnectionNotFoundException(CalendarType.GOOGLE_CALENDAR, userInformationId)

        val updatedGoogleCalendarConnection = updatedGoogleCalendarConnectionOpt.get()

        updatedGoogleCalendarConnection.keyword = updateDto.keyword

        return googleCalendarConnectionRepository.save(updatedGoogleCalendarConnection)
    }

    @Throws(CalendarConnectionNotFoundException::class)
    override fun deleteCalendarConnection(userInformationId: UUID) {
        val googleCalendarConnection = getCalendarConnection(userInformationId)

        googleCalendarConnectionRepository.delete(googleCalendarConnection)
    }

    override fun calendarConnectionExists(userInformationId: UUID): Boolean {
        return googleCalendarConnectionRepository.findById(userInformationId).isPresent
    }
}