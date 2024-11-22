package at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie

import at.szybbs.tacc.taccbackend.dto.teslaConnections.TeslaConnectionResponseDto
import java.util.*

data class TessieConnectionResponseDto(
    override val userInformationId: UUID,
    val vin: String,
    val accessToken: String,
) : TeslaConnectionResponseDto