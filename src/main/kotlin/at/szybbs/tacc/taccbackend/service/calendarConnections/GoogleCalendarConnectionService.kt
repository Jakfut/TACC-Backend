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
import at.szybbs.tacc.taccbackend.service.UserInformationService
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService
import org.springframework.stereotype.Service
import java.util.*

@Service
class GoogleCalendarConnectionService (
    private val googleCalendarConnectionRepository: GoogleCalendarConnectionRepository,
    private val userInformationService: UserInformationService,
    private val authorizedClientService: JdbcOAuth2AuthorizedClientService,
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
            keywordStart = creationDto.keywordStart,
            keywordEnd = creationDto.keywordEnd,
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

        calendarConnection.keywordStart = updateDto.keyword

        val updatedCalendarConnection = googleCalendarConnectionRepository.save(calendarConnection)

        return updatedCalendarConnection
    }

    @Throws(
        CalendarConnectionNotFoundException::class,
        UserInformationNotFoundException::class,
    )
    fun deleteCalendarConnection(userInformationId: UUID) {
        val googleCalendarConnection = getCalendarConnection(userInformationId)

        if (calendarTypeIsCurrentlyActive(userInformationId)) {
            userInformationService.setActiveCalendarConnectionTypeToNull(userInformationId)
        }

        googleCalendarConnectionRepository.delete(googleCalendarConnection)
    }

    @Throws(
        UserInformationNotFoundException::class,
        CalendarConnectionNotFoundException::class,
    )
    fun setCalendarConnectionToActive(userInformationId: UUID): UserInformation? {
        return userInformationService.setActiveCalendarConnectionType(userInformationId, CalendarType.GOOGLE_CALENDAR, googleCalendarConnectionRepository)
    }

    @Throws(
        CalendarConnectionNotFoundException::class,
    )
    fun disconnectGoogleCalendarApi(userInformationId: UUID) : GoogleCalendarConnection? {
        val calendarConnection = getCalendarConnection(userInformationId)

        val deletionSuccessful = runCatching {
            authorizedClientService.removeAuthorizedClient("google", calendarConnection.oauth2ConnectionId)
        }.isSuccess;

        // nothing changed
        if (calendarConnection.email == null && !deletionSuccessful) return null

        calendarConnection.email = null

        val updatedUserInformation = googleCalendarConnectionRepository.save(calendarConnection)

        return updatedUserInformation
    }

    fun calendarConnectionExists(userInformationId: UUID): Boolean {
        return googleCalendarConnectionRepository.findById(userInformationId).isPresent
    }

    @Throws(
        CalendarConnectionNotFoundException::class
    )
    fun calendarTypeIsCurrentlyActive(userInformationId: UUID): Boolean {
        val userInformation = userInformationService.getUserInformation(userInformationId)

        return userInformation.activeCalendarConnectionType == CalendarType.GOOGLE_CALENDAR
    }

    fun setGoogleCalendarEmail(userInformationId: UUID, email: String): GoogleCalendarConnection {
        val googleCalendarConnection = getCalendarConnection(userInformationId)
        googleCalendarConnection.email = email

        return googleCalendarConnectionRepository.save(googleCalendarConnection)
    }
}