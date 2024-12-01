package at.szybbs.tacc.taccbackend.client.teslaConnection

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaLocation
import java.util.UUID


interface TeslaConnectionClient {
    var userId: UUID

    fun getType(): TeslaConnectionType
    fun wake(): Boolean
    fun getLocation(): TeslaLocation
    fun getStatus(): String
    fun changeAcState(state: Boolean): Boolean
}