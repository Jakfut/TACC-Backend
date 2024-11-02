package at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie

import at.szybbs.tacc.taccbackend.dto.teslaConnections.TeslaConnectionCreationDto
import com.fasterxml.jackson.annotation.JsonProperty

data class TessieConnectionCreationDto(
    @JsonProperty("vin") var vin: String,
    @JsonProperty("access-token") var accessToken: String,
) : TeslaConnectionCreationDto