package at.szybbs.tacc.taccbackend.client.teslaConnection

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaLocation


interface TeslaConnectionClient {
    var vin: String
    var token: String

    fun getType(): TeslaConnectionType
    fun wake(): Boolean
    fun getLocation(): TeslaLocation
    fun getStatus(): String
}