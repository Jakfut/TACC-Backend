package at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar

import at.szybbs.tacc.taccbackend.dto.calendarConnections.CalendarConnectionCreationDto

data class GoogleCalendarConnectionCreationDto(
    val keyword: String,
) : CalendarConnectionCreationDto