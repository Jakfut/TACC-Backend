package at.szybbs.tacc.taccbackend.controller

import at.szybbs.tacc.taccbackend.dto.tesla.TeslaClimateActivationResponseDto
import at.szybbs.tacc.taccbackend.factory.TeslaConnectionFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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
class TeslaController (
    private val teslaConnectionFactory: TeslaConnectionFactory
) {

    @GetMapping("/reachable")
    @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)")
    fun isTeslaReachable(
        @PathVariable("user-information-id") userInformationId: UUID,
    ): ResponseEntity<Boolean> {
        val teslaConnectionClient = teslaConnectionFactory.createTeslaConnectionClient(userInformationId)
        teslaConnectionClient.getStatus()

        return ResponseEntity.ok(true)
    }

    @GetMapping("/location")
    @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)")
    fun getTeslaLocation(
        @PathVariable("user-information-id") userInformationId: UUID,
    ): ResponseEntity<String> {
        val teslaConnectionClient = teslaConnectionFactory.createTeslaConnectionClient(userInformationId)
        val location = teslaConnectionClient.getLocation()

        return ResponseEntity.ok(location)
    }

    @GetMapping("/climate/state")
    @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)")
    fun getTeslaClimateState(
        @PathVariable("user-information-id") userInformationId: UUID,
    ): ResponseEntity<Boolean> {
        val teslaConnectionClient = teslaConnectionFactory.createTeslaConnectionClient(userInformationId)
        val climateState = teslaConnectionClient.getAcStatus()

        return ResponseEntity.ok(climateState)
    }

    @PatchMapping("/climate/state")
    @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)")
    fun setTeslaClimateState(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody newState: Boolean
    ): ResponseEntity<Boolean> {
        val teslaConnectionClient = teslaConnectionFactory.createTeslaConnectionClient(userInformationId)
        val successful = teslaConnectionClient.changeAcState(newState)

        if (!successful) throw RuntimeException("Failed to change climate state to $newState")

        return ResponseEntity.ok(newState)
    }
}