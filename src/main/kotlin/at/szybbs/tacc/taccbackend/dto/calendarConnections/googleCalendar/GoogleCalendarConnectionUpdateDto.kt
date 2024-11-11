package at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar

import at.szybbs.tacc.taccbackend.dto.calendarConnections.CalendarConnectionUpdateDto
import at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar.GoogleCalendarConnection

data class GoogleCalendarConnectionUpdateDto(
    val keyword: String,
) : CalendarConnectionUpdateDto<GoogleCalendarConnection> {
    override fun hasChanged(calendarConnection: GoogleCalendarConnection): Boolean {
        return calendarConnection.keyword != keyword
    }
}