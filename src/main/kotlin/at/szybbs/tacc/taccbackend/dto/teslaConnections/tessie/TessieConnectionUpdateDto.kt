package at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie

import at.szybbs.tacc.taccbackend.dto.teslaConnections.TeslaConnectionUpdateDto
import com.fasterxml.jackson.annotation.JsonProperty

data class TessieConnectionUpdateDto(
    @JsonProperty("vin") var vin: String,
    @JsonProperty("access-token") var accessToken: String,
) : TeslaConnectionUpdateDto