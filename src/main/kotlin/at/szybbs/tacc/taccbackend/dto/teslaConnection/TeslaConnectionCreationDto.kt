package at.szybbs.tacc.taccbackend.dto.teslaConnection

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

data class TeslaConnectionCreationDto(
    @JsonProperty("active") var active: Boolean,
    @JsonProperty("config") var config: JsonNode
)
