package at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar

import at.szybbs.tacc.taccbackend.dto.calendarConnections.CalendarConnectionResponseDto
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class GoogleCalendarConnectionResponseDto(
    @JsonProperty("user_information_id") override var userInformationId: UUID,
    @JsonProperty("email") var email: String?,
    @JsonProperty("keyword") var keyword: String?,
) : CalendarConnectionResponseDto