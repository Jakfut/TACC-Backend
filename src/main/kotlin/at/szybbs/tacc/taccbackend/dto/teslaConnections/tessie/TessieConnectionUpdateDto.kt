package at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie

import at.szybbs.tacc.taccbackend.dto.teslaConnections.TeslaConnectionUpdateDto

data class TessieConnectionUpdateDto(
    val vin: String,
    val accessToken: String,
) : TeslaConnectionUpdateDto