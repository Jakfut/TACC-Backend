package at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar

data class GoogleCalendarListEntry(
    val id: String
)

data class GoogleCalendarListResponse(
    val items: List<GoogleCalendarListEntry>
)