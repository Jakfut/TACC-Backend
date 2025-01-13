package at.szybbs.tacc.taccbackend.dto.userInformation

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import java.util.UUID

data class UserInformationResponseDto(
    val id: UUID,
    val email: String,
    val noDestMinutes: Int,
    val ccRuntimeMinutes: Int,
    val arrivalBufferMinutes: Int,
    val activeCalendarConnectionType: CalendarType?,
    val activeTeslaConnectionType: TeslaConnectionType?,
    val oauth2Session: String?
)
