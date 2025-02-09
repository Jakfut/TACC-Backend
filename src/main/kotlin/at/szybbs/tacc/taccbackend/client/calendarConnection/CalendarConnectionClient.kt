package at.szybbs.tacc.taccbackend.client.calendarConnection

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarEvent
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import java.time.Instant
import java.util.*

interface CalendarConnectionClient {
    var userId: UUID

    fun getType(): CalendarType
    fun getCalendarIdList(): List<String>
    fun getEvents(calendarId: String, timeMin: Instant): List<CalendarEvent>
    fun getEventWithKeyword(calendarId: String, timeMin: Instant, keyword: String): List<CalendarEvent>
    fun getAllEventsWithKeywordStart(timeMin: Instant): List<CalendarEvent>
    fun getAllEventsWithKeywordEnd(timeMin: Instant): List<CalendarEvent>
}