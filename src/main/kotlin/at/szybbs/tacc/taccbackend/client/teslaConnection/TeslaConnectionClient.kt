package at.szybbs.tacc.taccbackend.client.teslaConnection

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import java.util.UUID


interface TeslaConnectionClient {
    var userId: UUID

    fun getType(): TeslaConnectionType
    fun wake(): Boolean
    fun getLocation(): String
    fun getStatus(): String
    fun changeAcState(state: Boolean): Boolean
}