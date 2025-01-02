package at.szybbs.tacc.taccbackend.client.calendarConnection

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarEvent
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar.CalendarListResponse
import at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar.GoogleCalendarEventsResponse
import at.szybbs.tacc.taccbackend.entity.calendarConnections.mapGoogleToCalendarEvent
import org.springframework.context.annotation.Scope
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor
import org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId
import org.springframework.security.oauth2.client.web.client.RequestAttributePrincipalResolver.principal
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import java.util.*


@Component
@Scope("prototype")
class GoogleConnectionClient(
    authorizedClientManager: AuthorizedClientServiceOAuth2AuthorizedClientManager
): CalendarConnectionClient {
    override lateinit var userId: UUID

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
            .retrieve()
            .toEntity<CalendarListResponse>()

        if (result.statusCode.is4xxClientError) {
            throw Exception("Unauthorized")
            // TODO handle error
        }

        val calendarListResponse: CalendarListResponse? = result.body

        return calendarListResponse?.items?.map { it.id } ?: emptyList()
    }

    override fun getEvents(calendarId: String): List<CalendarEvent> {
        val result = restClient.get()
            .uri("/calendars/$calendarId/events")
            .attributes(clientRegistrationId("google"))
            .attributes(principal(userId.toString()))
            .retrieve()
            .toEntity<GoogleCalendarEventsResponse>()

        if (result.statusCode.is4xxClientError) {
            throw Exception("Unauthorized")
            // TODO handle error
        }

        val calendarEventsResponse: GoogleCalendarEventsResponse? = result.body

        if (calendarEventsResponse != null) {
            val calendarEvents: List<CalendarEvent> = calendarEventsResponse.items.map { mapGoogleToCalendarEvent(it) }
            return calendarEvents
        }
        return emptyList()
    }

    override fun getEventWithKeyword(calendarId: String, keyword: String): List<CalendarEvent> {
        val result = restClient.get()
            .uri("/calendars/$calendarId/events?q=$keyword")
            .attributes(clientRegistrationId("google"))
            .attributes(principal(userId.toString()))
            .retrieve()
            .toEntity<GoogleCalendarEventsResponse>()

        if (result.statusCode.is4xxClientError) {
            throw Exception("Unauthorized")
            // TODO handle error
        }

        val calendarEventsResponse: GoogleCalendarEventsResponse? = result.body

        if (calendarEventsResponse != null) {
            val calendarEvents: List<CalendarEvent> = calendarEventsResponse.items.map { mapGoogleToCalendarEvent(it) }
            return calendarEvents
        }

        return emptyList()
    }
}