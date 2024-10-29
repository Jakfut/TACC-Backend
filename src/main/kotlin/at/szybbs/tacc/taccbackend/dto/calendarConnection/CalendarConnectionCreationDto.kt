package at.szybbs.tacc.taccbackend.dto.calendarConnection

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

data class CalendarConnectionCreationDto(
    @JsonProperty("active") var active: Boolean,
    @JsonProperty("config") var config: JsonNode
)
