package at.szybbs.tacc.taccbackend.dto.teslaConnections

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnection

/**
 * Marker interface for DTOs used to update Tesla connections.
 * This can be extended in the future if properties are needed.
 */
interface TeslaConnectionUpdateDto<T: TeslaConnection> {
    fun hasChanged(teslaConnection: T): Boolean
}