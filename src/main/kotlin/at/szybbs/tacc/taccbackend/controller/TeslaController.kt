package at.szybbs.tacc.taccbackend.controller

import at.szybbs.tacc.taccbackend.dto.tesla.TeslaClimateActivationResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime
import java.util.UUID

@RestController
@RequestMapping("/api/user/{user-information-id}/tesla")
class TeslaController {

    @GetMapping("/reachable")
    fun teslaIsReachable(
        @PathVariable("user-information-id") userInformationId: UUID,
    ): ResponseEntity<Boolean> {
        // TODO: call http-client

        return ResponseEntity.ok(false)
    }

    @GetMapping("/location")
    fun teslaLocation(
        @PathVariable("user-information-id") userInformationId: UUID,
    ): ResponseEntity<String> {
        // TODO: call http-client

        return ResponseEntity.ok("Address")
    }

    @GetMapping("/climate/state")
    fun teslaClimateIsActive(
        @PathVariable("user-information-id") userInformationId: UUID,
    ): ResponseEntity<Boolean> {
        // TODO: call http-client

        return ResponseEntity.ok(false)
    }

    @PatchMapping("/climate/state")
    fun setTeslaClimateToInactive(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody state: Boolean
    ): ResponseEntity<Boolean> {
        // TODO: call http-client

        return ResponseEntity.ok(state)
    }

    @GetMapping("/climate/upcoming-activations")
    fun upcomingActivations(
        @PathVariable("user-information-id") userInformationId: UUID
    ): ResponseEntity<List<TeslaClimateActivationResponseDto>> {
        // TODO: call http-client

        val list = arrayListOf(
            TeslaClimateActivationResponseDto(
                climateActivationTime = ZonedDateTime.now(),
                departureTime = ZonedDateTime.now().plusMinutes(10),
                arrivalTime = ZonedDateTime.now().plusMinutes(50),
                eventStartTime = ZonedDateTime.now().plusMinutes(60),
                eventLocation = "Address"
            )
        )

        return ResponseEntity.ok(list)
    }
}