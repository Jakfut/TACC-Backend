package at.szybbs.tacc.taccbackend.controller.teslaConnections

import at.szybbs.tacc.taccbackend.dto.teslaConnections.TeslaConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.teslaConnections.TeslaConnectionResponseDto
import at.szybbs.tacc.taccbackend.dto.teslaConnections.TeslaConnectionUpdateDto
import org.springframework.http.ResponseEntity
import java.util.UUID

/**
 * Controller interface for managing Tesla connections in the system.
 *
 * This interface defines methods for retrieving, creating, updating,
 * and deleting Tesla connections associated with user information.
 *
 * @param R Type of the creation DTO for Tesla connections.
 * @param E Type of the response DTO for Tesla connections.
 * @param U Type of the update DTO for Tesla connections.
 */
interface TeslaConnectionController<R: TeslaConnectionCreationDto, E: TeslaConnectionResponseDto, U: TeslaConnectionUpdateDto> {
    fun getTeslaConnection(userInformationId: UUID) : ResponseEntity<E>
    fun createTeslaConnection(userInformationId: UUID, creationDto: R) : ResponseEntity<E>
    fun updateTeslaConnection(userInformationId: UUID, updateDto: U) : ResponseEntity<E>
    fun deleteTeslaConnection(userInformationId: UUID) : ResponseEntity<Void>
}