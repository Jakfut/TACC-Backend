package at.szybbs.tacc.taccbackend.entity.calendarConnections

import at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar.GoogleCalendarEventItem
import java.time.Instant

data class CalendarEvent(
    val description: String,
    val location: String?,
    val start: Instant,
    val end: Instant
)

fun mapGoogleToCalendarEvent(item: GoogleCalendarEventItem): CalendarEvent {
    val startInstant = item.start.dateTime ?: throw IllegalArgumentException("Invalid event data: Start time is missing for event")
    val endInstant = item.end.dateTime ?: throw IllegalArgumentException("Invalid event data: End time is missing for event")

    return CalendarEvent(
        description = item.description ?: "",
        location = item.location ?: "",
        start = startInstant,
        end = endInstant
    )
}