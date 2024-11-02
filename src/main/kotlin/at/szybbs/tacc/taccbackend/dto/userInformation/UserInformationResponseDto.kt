package at.szybbs.tacc.taccbackend.dto.userInformation

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class UserInformationResponseDto(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("email") val email: String,
    @JsonProperty("no_dest_minutes") val noDestMinutes: Int,
    @JsonProperty("cc_runtime_minutes") val ccRuntimeMinutes: Int,
    @JsonProperty("arrival_buffer_minutes") val arrivalBufferMinutes: Int,
    @JsonProperty("active_calendar_connection_type") val activeCalendarConnectionType: CalendarType?,
    @JsonProperty("active_tesla_connection_type") val activeTeslaConnectionType: TeslaConnectionType?,
)
