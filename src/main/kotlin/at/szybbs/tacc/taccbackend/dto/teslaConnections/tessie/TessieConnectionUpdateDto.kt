package at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie

import at.szybbs.tacc.taccbackend.dto.teslaConnections.TeslaConnectionUpdateDto
import at.szybbs.tacc.taccbackend.entity.teslaConnections.tessie.TessieConnection

data class TessieConnectionUpdateDto(
    val vin: String,
    val accessToken: String,
) : TeslaConnectionUpdateDto<TessieConnection> {
    override fun hasChanged(teslaConnection: TessieConnection): Boolean {
        return teslaConnection.vin != vin || teslaConnection.accessToken != accessToken
    }
}