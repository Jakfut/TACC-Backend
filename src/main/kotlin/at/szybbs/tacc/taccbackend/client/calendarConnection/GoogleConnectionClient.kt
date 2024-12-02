package at.szybbs.tacc.taccbackend.client.calendarConnection

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarEvent
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.service.calendarConnections.GoogleCalendarConnectionService
import org.springframework.context.annotation.Scope
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
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
    private val googleCalendarConnectionService: GoogleCalendarConnectionService,
    authorizedClientManager: OAuth2AuthorizedClientManager
): CalendarConnectionClient {
    override lateinit var userId: UUID

    private val restClient = RestClient.builder()
        .baseUrl("https://www.googleapis.com/calendar/v3")
        .defaultHeaders { it.set("Content-Type", "application/json") }
        .requestInterceptor(OAuth2ClientHttpRequestInterceptor(authorizedClientManager))
        .build()

    override fun getType(): CalendarType {
        return CalendarType.GOOGLE_CALENDAR
    }

    override fun getCalendarList(): List<String> {
        val result = restClient.get()
            .uri("/users/me/calendarList")
            .attributes(clientRegistrationId("google"))
            .attributes(principal(userId.toString()))
            .retrieve()
            .toEntity<String>()

        if (result.statusCode.is4xxClientError) {
            throw Exception("Unauthorized")
            // TODO handle error
        }

        println("CalendarList:" + result.body)

        return listOf()
    }

    override fun getEvents(calendarId: String): List<CalendarEvent> {
        TODO("Not yet implemented")
    }

    override fun getEventWithKeyword(calendarId: String, keyword: String): List<CalendarEvent> {
        TODO("Not yet implemented")
    }
}