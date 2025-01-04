package at.szybbs.tacc.taccbackend.client.calendarConnection

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarEvent
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar.GoogleCalendarListResponse
import at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar.GoogleCalendarEventsResponse
import at.szybbs.tacc.taccbackend.entity.calendarConnections.mapGoogleToCalendarEvent
import at.szybbs.tacc.taccbackend.service.calendarConnections.GoogleCalendarConnectionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor
import org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId
import org.springframework.security.oauth2.client.web.client.RequestAttributePrincipalResolver.principal
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.time.Instant
import java.util.*


@Component
@Scope("prototype")
class GoogleConnectionClient(
    authorizedClientManager: AuthorizedClientServiceOAuth2AuthorizedClientManager,
    googleCalendarConnectionService: GoogleCalendarConnectionService
): CalendarConnectionClient {
    override lateinit var userId: UUID

    private val keyword: String by lazy { googleCalendarConnectionService.getCalendarConnection(userId).keyword }

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val restClient = RestClient.builder()
        .baseUrl("https://www.googleapis.com/calendar/v3")
        .defaultHeaders { it.set("Content-Type", "application/json") }
        .requestInterceptor(OAuth2ClientHttpRequestInterceptor(authorizedClientManager).apply {
            setPrincipalResolver(at.szybbs.tacc.taccbackend.client.TaccPrincipalResolver())
        })
        .build()

    override fun getType(): CalendarType {
        return CalendarType.GOOGLE_CALENDAR
    }

    override fun getCalendarIdList(): List<String> {
        val result = restClient.get()
            .uri("/users/me/calendarList")
            .attributes(clientRegistrationId("google"))
            .attributes(principal(userId.toString()))
            .exchange{ _, response ->
                if (response.statusCode.is2xxSuccessful) {
                    response.bodyTo(GoogleCalendarListResponse::class.java)
                } else {
                    logger.warn("Failed to retrieve calendar list for user $userId: Status code: ${response.statusCode}, Body: ${response.body}")
                    null
                }
            }

        return result?.items?.map { it.id } ?: emptyList()
    }

    override fun getEvents(calendarId: String, timeMin: Instant): List<CalendarEvent> {
        val result = restClient.get()
            .uri("/calendars/$calendarId/events?timeMin=${timeMin}")
            .attributes(clientRegistrationId("google"))
            .attributes(principal(userId.toString()))
            .exchange{ _, response ->
                if (response.statusCode.is2xxSuccessful) {
                    response.bodyTo(GoogleCalendarEventsResponse::class.java)
                } else {
                    logger.warn("Failed to retrieve events for calendar $calendarId for user $userId: Status code: ${response.statusCode}, Body: ${response.body}")
                    null
                }
            }

        return result?.items?.map { mapGoogleToCalendarEvent(it) } ?: emptyList()
    }

    override fun getEventWithKeyword(calendarId: String, timeMin: Instant): List<CalendarEvent> {
        val result = restClient.get()
            .uri("/calendars/$calendarId/events?q=$keyword&timeMin=${timeMin}")
            .attributes(clientRegistrationId("google"))
            .attributes(principal(userId.toString()))
            .exchange{ _, response ->
                if (response.statusCode.is2xxSuccessful) {
                    response.bodyTo(GoogleCalendarEventsResponse::class.java)
                } else {
                    logger.warn("Failed to retrieve events for calendar $calendarId for user $userId: Status code: ${response.statusCode}, Body: ${response.body}")
                    null
                }
            }

        return result?.items?.map { mapGoogleToCalendarEvent(it) } ?: emptyList()
    }

    override fun getAllEventsWithKeyword(timeMin: Instant): List<CalendarEvent> {
        val calendarIdList = getCalendarIdList()
        return calendarIdList.flatMap { getEventWithKeyword(it, timeMin) }
    }
}