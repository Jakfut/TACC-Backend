package at.szybbs.tacc.taccbackend.client.calendarConnection

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarEvent
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.service.calendarConnections.GoogleCalendarConnectionService
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.util.*

@Component
@Scope("prototype")
class GoogleConnectionClient(
    private val googleCalendarConnectionService: GoogleCalendarConnectionService
): CalendarConnectionClient {
    override lateinit var userId: UUID

    private val restClient = RestClient.builder()
        .baseUrl("https://www.googleapis.com/calendar/v3/")
        .defaultHeaders { it.set("Content-Type", "application/json") }
        .build()

    override fun getType(): CalendarType {
        return CalendarType.GOOGLE_CALENDAR
    }

    override fun getCalendarList(): List<String> {
        TODO("Not yet implemented")
    }

    override fun getEvents(calendarId: String): List<CalendarEvent> {
        TODO("Not yet implemented")
    }

    override fun getEventWithKeyword(calendarId: String, keyword: String): List<CalendarEvent> {
        TODO("Not yet implemented")
    }
}