package at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie

import at.szybbs.tacc.taccbackend.dto.teslaConnections.TeslaConnectionResponseDto
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class TessieConnectionResponseDto(
    @JsonProperty("user_information_id") override var userInformationId: UUID,
    @JsonProperty("vin") var vin: String?,
    @JsonProperty("access-token") var accessToken: String?,
) : TeslaConnectionResponseDto