package at.szybbs.tacc.taccbackend.service.teslaConnections

import at.szybbs.tacc.taccbackend.dto.teslaConnections.TeslaConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.teslaConnections.TeslaConnectionUpdateDto
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnection
import java.util.UUID

/**
 * Service interface for managing Tesla connections in the system.
 *
 * @param T Type of the Tesla connection entity, used as the return value for retrieval and creation methods.
 * @param R Type of the creation DTO for Tesla connections.
 * @param U Type of the update DTO for Tesla connections.
 */
interface TeslaConnectionService<T: TeslaConnection, R: TeslaConnectionCreationDto, U: TeslaConnectionUpdateDto> {
    fun getTeslaConnection(userInformationId: UUID) : T
    fun createTeslaConnection(userInformationId: UUID, creationDto: R) : T
    fun updateTeslaConnection(userInformationId: UUID, updateDto: U) : T
    fun deleteTeslaConnection(userInformationId: UUID)
    fun teslaConnectionExists(userInformationId: UUID): Boolean
}