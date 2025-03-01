package at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.Instant

data class GoogleEventDateTime(
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    val dateTime: Instant?,
    val timeZone: String?
)

data class GoogleCalendarEventItem(
    val description: String?,
    val location: String?,
    val start: GoogleEventDateTime,
    val end: GoogleEventDateTime
)

data class GoogleCalendarEventsDTO(
    val items: List<GoogleCalendarEventItem>
)