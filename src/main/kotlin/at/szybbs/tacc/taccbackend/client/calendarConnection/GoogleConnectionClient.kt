package at.szybbs.tacc.taccbackend.client.calendarConnection

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarEvent
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar.GoogleCalendarListResponse
import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarEventsDTO
import at.szybbs.tacc.taccbackend.entity.calendarConnections.mapGoogleToCalendarEvent
import at.szybbs.tacc.taccbackend.service.calendarConnections.GoogleCalendarConnectionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor
import org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId
import org.springframework.security.oauth2.client.web.client.RequestAttributePrincipalResolver
import org.springframework.security.oauth2.client.web.client.RequestAttributePrincipalResolver.principal
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import java.time.Instant
import java.util.*


@Component
@Scope("prototype")
class GoogleConnectionClient(
    authorizedClientManager: AuthorizedClientServiceOAuth2AuthorizedClientManager,
    googleCalendarConnectionService: GoogleCalendarConnectionService,
): CalendarConnectionClient {
    override lateinit var userId: UUID

    private val keywordStart: String by lazy { googleCalendarConnectionService.getCalendarConnection(userId).keywordStart }
    private val keywordEnd: String by lazy { googleCalendarConnectionService.getCalendarConnection(userId).keywordEnd }

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val restClient = RestClient.builder()
        .baseUrl("https://www.googleapis.com/calendar/v3")
        .defaultHeaders { it.set("Content-Type", "application/json") }
        /*.requestInterceptor(OAuth2ClientHttpRequestInterceptor(authorizedClientManager).apply {
            setPrincipalResolver(at.szybbs.tacc.taccbackend.client.TaccPrincipalResolver())
        })*/
        .requestInterceptor(OAuth2ClientHttpRequestInterceptor(authorizedClientManager).apply {
            setPrincipalResolver(RequestAttributePrincipalResolver())
        })
        .build()

    private val oauth2ConnectionId: String by lazy { googleCalendarConnectionService.getCalendarConnection(userId).oauth2ConnectionId }

    override fun getType(): CalendarType {
        return CalendarType.GOOGLE_CALENDAR
    }

    override fun getCalendarIdList(): List<String> {
        val result = restClient.get()
            .uri("/users/me/calendarList")
            .attributes(clientRegistrationId("google"))
            .attributes(principal(oauth2ConnectionId))
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
            .attributes(principal(oauth2ConnectionId))
            .exchange{ _, response ->
                if (response.statusCode.is2xxSuccessful) {
                    response.bodyTo(GoogleCalendarEventsDTO::class.java)
                } else {
                    logger.warn("Failed to retrieve events for calendar $calendarId for user $userId: Status code: ${response.statusCode}, Body: ${response.body}")
                    null
                }
            }

        return result?.items?.map { mapGoogleToCalendarEvent(it) } ?: emptyList()
    }

    override fun getEventWithKeyword(calendarId: String, timeMin: Instant, keyword: String): List<CalendarEvent> {
        logger.info("Getting events with keyword $keyword for calendar $calendarId, user $userId, timeMin: $timeMin")
        val result = restClient.get()
            .uri("/calendars/$calendarId/events?q=$keyword&singleEvents=true&timeMin=${timeMin}&timeMax=${timeMin.plusSeconds(7 * 24 * 60 * 60)}")
            .attributes(clientRegistrationId("google"))
            .attributes(principal(oauth2ConnectionId))
            .exchange{ _, response ->
                if (response.statusCode.is2xxSuccessful) {
                    response.bodyTo(GoogleCalendarEventsDTO::class.java)
                } else {
                    logger.warn("Failed to retrieve events for calendar $calendarId for user $userId: Status code: ${response.statusCode}, Body: ${response.body}")
                    null
                }
            }

        return result?.items?.map { mapGoogleToCalendarEvent(it) } ?: emptyList()
    }

    override fun getAllEventsWithKeywordStart(timeMin: Instant): List<CalendarEvent> {
        logger.info("Getting all events with keyword $keywordStart for user $userId, timeMin: $timeMin")
        val calendarIdList = getCalendarIdList()
        return calendarIdList.flatMap { getEventWithKeyword(it, timeMin, keywordStart) }
    }

    override fun getAllEventsWithKeywordEnd(timeMin: Instant): List<CalendarEvent> {
        logger.info("Getting all events with keyword $keywordEnd for user $userId, timeMin: $timeMin")
        val calendarIdList = getCalendarIdList()
        return calendarIdList.flatMap { getEventWithKeyword(it, timeMin, keywordEnd) }
    }
}

fun getGoogleCalendarEmail(userId: UUID, applicationContext: ApplicationContext): String {
    val googleCalendarConnectionService = applicationContext.getBean(GoogleCalendarConnectionService::class.java)
    val authorizedClientManager = applicationContext.getBean(AuthorizedClientServiceOAuth2AuthorizedClientManager::class.java)

    val oauth2ConnectionId: String by lazy { googleCalendarConnectionService.getCalendarConnection(userId).oauth2ConnectionId }

    val restClient = RestClient.builder()
        .defaultHeaders { it.set("Content-Type", "application/json") }
        .requestInterceptor(OAuth2ClientHttpRequestInterceptor(authorizedClientManager).apply {
            setPrincipalResolver(RequestAttributePrincipalResolver())
        })
        .build()

    val result = restClient.get()
        .uri("https://www.googleapis.com/oauth2/v3/userinfo")
        .attributes(clientRegistrationId("google"))
        .attributes(principal(oauth2ConnectionId))
        .retrieve()
        .toEntity<Map<String, String>>()

    return result.body?.get("email") ?: throw Exception("Failed to get email")
}