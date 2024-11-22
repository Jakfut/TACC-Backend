package at.szybbs.tacc.taccbackend.controller.teslaConnections

import at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie.TessieConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie.TessieConnectionResponseDto
import at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie.TessieConnectionUpdateDto
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationResponseDto
import at.szybbs.tacc.taccbackend.service.teslaConnections.TessieConnectionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * Controller for managing Tessie connections associated with user information.
 *
 * This REST controller provides endpoints for creating, retrieving, updating, and deleting
 * Tessie connections for a specified user.
 *
 * @property tessieConnectionService Service for managing Tessie connections.
 */
@RestController
@RequestMapping("/api/user/{user-information-id}/tesla-connections/tessie")
class TessieConnectionController (
    private val tessieConnectionService: TessieConnectionService
) {

    @GetMapping
    fun getTeslaConnection(
        @PathVariable("user-information-id") userInformationId: UUID,
    ) : ResponseEntity<TessieConnectionResponseDto> {
        val responseDto = tessieConnectionService.getTeslaConnection(userInformationId)
            .toResponseDto()

        return ResponseEntity.ok(responseDto)
    }

    @DeleteMapping
    fun deleteTeslaConnection(
        @PathVariable("user-information-id") userInformationId: UUID
    ): ResponseEntity<Void> {
        tessieConnectionService.deleteTeslaConnection(userInformationId)

        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/active")
    fun setTeslaConnectionToActive(
        @PathVariable("user-information-id") userInformationId: UUID
    ): ResponseEntity<UserInformationResponseDto> {
        val responseDto = tessieConnectionService.setTeslaConnectionToActive(userInformationId)
            ?.toResponseDto() ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(responseDto)
    }

    @PatchMapping
    fun updateTeslaConnectionPublicFields(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody updateDto: TessieConnectionUpdateDto
    ): ResponseEntity<TessieConnectionResponseDto> {
        val responseDto = tessieConnectionService.updateTeslaConnectionPublicFields(userInformationId, updateDto)
            ?.toResponseDto() ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(responseDto)
    }

    @PostMapping
    fun createTeslaConnection(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody creationDto: TessieConnectionCreationDto
    ): ResponseEntity<TessieConnectionResponseDto> {
        val responseDto = tessieConnectionService.createTeslaConnection(userInformationId, creationDto)
            .toResponseDto()

        return ResponseEntity.ok(responseDto)
    }

    @GetMapping("/reachable")
    fun checkTeslaReachability(
        @PathVariable("user-information-id") userInformationId: UUID
    ): ResponseEntity<Boolean> {
        val isReachable = tessieConnectionService.isReachable(userInformationId)
        return ResponseEntity.ok(isReachable)
    }
}