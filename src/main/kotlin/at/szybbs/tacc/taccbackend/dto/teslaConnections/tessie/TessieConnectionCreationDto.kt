package at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie

import at.szybbs.tacc.taccbackend.dto.teslaConnections.TeslaConnectionCreationDto

data class TessieConnectionCreationDto(
    val vin: String,
    val accessToken: String,
) : TeslaConnectionCreationDto