package at.szybbs.tacc.taccbackend.dto.calendarConnection

import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarConnection
import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import java.util.UUID

data class CalendarConnectionResponseDto(
    @JsonProperty("user_information_id") var userInformationId: UUID,
    @JsonProperty("calendar_type") var calendarType: CalendarType,
    @JsonProperty("active") var active: Boolean,
    @JsonProperty("config") var config: JsonNode
)

fun CalendarConnection.toResponseDto(): CalendarConnectionResponseDto {
    return CalendarConnectionResponseDto(
        userInformationId = this.id.userInformationId,
        calendarType = this.id.type,
        active = this.active,
        config = this.config,
    )
}

fun List<CalendarConnection>.toResponseDto(): List<CalendarConnectionResponseDto> {
    return this.map { it.toResponseDto() }
}