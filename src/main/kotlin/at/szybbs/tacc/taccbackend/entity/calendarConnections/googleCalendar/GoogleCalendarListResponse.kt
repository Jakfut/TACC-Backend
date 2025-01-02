package at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar

data class CalendarListEntry(
    val id: String
)

data class CalendarListResponse(
    val items: List<CalendarListEntry>
)