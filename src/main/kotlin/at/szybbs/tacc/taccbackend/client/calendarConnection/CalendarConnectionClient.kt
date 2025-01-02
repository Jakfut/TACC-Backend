package at.szybbs.tacc.taccbackend.client.calendarConnection

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarEvent
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import java.util.*

interface CalendarConnectionClient {
    var userId: UUID

    fun getType(): CalendarType
    fun getCalendarIdList(): List<String>
    fun getEvents(calendarId: String): List<CalendarEvent>
    fun getEventWithKeyword(calendarId: String, keyword: String): List<CalendarEvent>
}