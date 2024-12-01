package at.szybbs.tacc.taccbackend.entity.calendarConnections

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaLocation
import java.time.Instant

data class CalendarEvent(
    val description: String,
    val location: TeslaLocation,
    val start: Instant,
    val end: Instant
)