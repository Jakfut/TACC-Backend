package at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar

import at.szybbs.tacc.taccbackend.dto.calendarConnections.CalendarConnectionResponseDto
import java.util.UUID

data class GoogleCalendarConnectionResponseDto(
    override val userInformationId: UUID,
    val email: String?,
    val keyword: String,
) : CalendarConnectionResponseDto