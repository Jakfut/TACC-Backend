package at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar

import at.szybbs.tacc.taccbackend.dto.calendarConnections.CalendarConnectionUpdateDto
import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleCalendarConnectionUpdateDto(
    @JsonProperty("keyword") var keyword: String,
) : CalendarConnectionUpdateDto