package at.szybbs.tacc.taccbackend.controller.teslaConnection

import at.szybbs.tacc.taccbackend.dto.teslaConnection.TeslaConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.teslaConnection.TeslaConnectionResponseDto
import at.szybbs.tacc.taccbackend.dto.teslaConnection.toResponseDto
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnectionId
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnectionType
import at.szybbs.tacc.taccbackend.service.teslaConnection.TeslaConnectionService
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/user/{userInformationId}/tesla-connection")
class TeslaConnectionController (
    private val teslaConnectionService: TeslaConnectionService
) {
    @GetMapping
    fun getAllTeslaConnections(
        @PathVariable userInformationId: UUID
    ) : ResponseEntity<List<TeslaConnectionResponseDto>> {
        val teslaConnectionResponseDtos = teslaConnectionService.getAllTeslaConnections(userInformationId)
            .toResponseDto()

        return ResponseEntity.ok(teslaConnectionResponseDtos)
    }

    @GetMapping("/{teslaConnectionType}")
    fun getTeslaConnection(
        @PathVariable userInformationId: UUID,
        @PathVariable teslaConnectionType: TeslaConnectionType
    ) : ResponseEntity<TeslaConnectionResponseDto> {
        val teslaConnectionResponseDto = teslaConnectionService.getTeslaConnection(
            TeslaConnectionId(
                type = teslaConnectionType,
                userInformationId = userInformationId
            )
        ).toResponseDto()

        return ResponseEntity.ok(teslaConnectionResponseDto)
    }

    @GetMapping("/active")
    fun getActiveTeslaConnection(
        @PathVariable userInformationId: UUID
    ) : ResponseEntity<TeslaConnectionResponseDto> {
        val teslaConnectionResponseDto = teslaConnectionService.getActiveTeslaConnection(userInformationId)
            .toResponseDto()

        return ResponseEntity.ok(teslaConnectionResponseDto)
    }

    @PostMapping("/{teslaConnectionType}")
    fun createTeslaConnection(
        @PathVariable userInformationId: UUID,
        @PathVariable teslaConnectionType: TeslaConnectionType,
        @RequestBody teslaConnectionCreationDto: TeslaConnectionCreationDto
    ) : ResponseEntity<TeslaConnectionResponseDto> {
        val teslaConnectionResponseDto = teslaConnectionService.createTeslaConnectionFromUserInput(
            teslaConnectionId = TeslaConnectionId(
                type = teslaConnectionType,
                userInformationId = userInformationId
            ),
            teslaConnectionCreationDto = teslaConnectionCreationDto
        ).toResponseDto()

        return ResponseEntity.ok(teslaConnectionResponseDto)
    }

    @PatchMapping("/{teslaConnectionType}/active")
    fun setTeslaConnectionToActive(
        @PathVariable userInformationId: UUID,
        @PathVariable teslaConnectionType: TeslaConnectionType
    ) : ResponseEntity<TeslaConnectionResponseDto> {
        val teslaConnectionResponseDto = teslaConnectionService.setTeslaConnectionToActive(
            TeslaConnectionId(
                type = teslaConnectionType,
                userInformationId = userInformationId
            )
        ).toResponseDto()

        return ResponseEntity.ok(teslaConnectionResponseDto)
    }

    @PatchMapping("/{teslaConnectionType}/inactive")
    fun setTeslaConnectionToInactive(
        @PathVariable userInformationId: UUID,
        @PathVariable teslaConnectionType: TeslaConnectionType
    ) : ResponseEntity<TeslaConnectionResponseDto> {
        val teslaConnectionResponseDto = teslaConnectionService.setTeslaConnectionToInactive(
            TeslaConnectionId(
                type = teslaConnectionType,
                userInformationId = userInformationId
            )
        ).toResponseDto()

        return ResponseEntity.ok(teslaConnectionResponseDto)
    }

    @PatchMapping("/{teslaConnectionType}/config")
    fun updateTeslaConnectionConfig(
        @PathVariable userInformationId: UUID,
        @PathVariable teslaConnectionType: TeslaConnectionType,
        @RequestBody config: JsonNode
    ) : ResponseEntity<TeslaConnectionResponseDto> {
        val teslaConnectionResponseDto = teslaConnectionService.updateTeslaConnectionConfigFromUserInput(
            teslaConnectionId = TeslaConnectionId(
                type = teslaConnectionType,
                userInformationId = userInformationId
            ),
            newConfig = config
        ).toResponseDto()

        return ResponseEntity.ok(teslaConnectionResponseDto)
    }

    @DeleteMapping("/{teslaConnectionType}")
    fun deleteTeslaConnection(
        @PathVariable userInformationId: UUID,
        @PathVariable teslaConnectionType: TeslaConnectionType
    ) : ResponseEntity<Void> {
        teslaConnectionService.deleteTeslaConnection(
            TeslaConnectionId(
            type = teslaConnectionType,
            userInformationId = userInformationId
        )
        )

        return ResponseEntity.noContent().build()
    }
}