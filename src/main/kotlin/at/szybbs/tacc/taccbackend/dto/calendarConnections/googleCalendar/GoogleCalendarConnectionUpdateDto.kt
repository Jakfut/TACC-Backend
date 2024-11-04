package at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar

import at.szybbs.tacc.taccbackend.dto.calendarConnections.CalendarConnectionUpdateDto

data class GoogleCalendarConnectionUpdateDto(
    val keyword: String,
) : CalendarConnectionUpdateDto