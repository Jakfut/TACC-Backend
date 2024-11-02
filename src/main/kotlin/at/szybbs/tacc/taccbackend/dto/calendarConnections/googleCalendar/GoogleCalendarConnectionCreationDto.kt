package at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar

import at.szybbs.tacc.taccbackend.dto.calendarConnections.CalendarConnectionCreationDto
import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleCalendarConnectionCreationDto(
    @JsonProperty("keyword") var keyword: String,
) : CalendarConnectionCreationDto