package at.szybbs.tacc.taccbackend.controller

import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationCreationDto
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationResponseDto
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationUpdateDefaultValuesDto
import at.szybbs.tacc.taccbackend.service.UserInformationService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/user/{user-information-id}")
class UserInformationController (
    private val userInformationService: UserInformationService
) {

    @GetMapping
    fun getUserInformation(
        @PathVariable("user-information-id") userInformationId: UUID
    ) : ResponseEntity<UserInformationResponseDto> {
        val responseDto = userInformationService.getUserInformation(userInformationId)
            .toResponseDto()

        return ResponseEntity.ok(responseDto)
    }

    // @PreAuthorize("hasRole('ADMIN...')") -> TODO: only allow admin/service account, implementing automatic creation
    @PostMapping
    fun createUserInformation(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody creationDto: UserInformationCreationDto?,
        authentication: Authentication?
    ) : ResponseEntity<UserInformationResponseDto> {

        var email : String? = null

        if (authentication is JwtAuthenticationToken) {
            email = authentication.tokenAttributes["email"]?.toString()
        }
        else {
            email = "testMail@mail.com" // TODO: remove later in production
        }

        if (email == null) return ResponseEntity.badRequest().body(null) // TODO: make authentication NOT NULLABLE

        val responseDto = userInformationService.createUserInformation(
            userInformationId,
            creationDto,
            email
        ).toResponseDto()

        return ResponseEntity.ok(responseDto)
    }

    // @PreAuthorize("hasRole('ADMIN...')") -> TODO: only allow admin/service account, implementing automatic deletion
    @DeleteMapping
    fun deleteUserInformation(
        @PathVariable("user-information-id") userInformationId: UUID
    ) : ResponseEntity<Void> {
        userInformationService.deleteUserInformation(userInformationId)

        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/default-values")
    fun updateUserInformationDefaultValues(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody updateDto: UserInformationUpdateDefaultValuesDto
    ) : ResponseEntity<UserInformationResponseDto> {
        val responseDto = userInformationService.updateUserInformationDefaultValues(userInformationId, updateDto)
            ?.toResponseDto() ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(responseDto)
    }

    @PatchMapping("calendar-connections/deactivate")
    fun setActiveCalendarConnectionToNull(
        @PathVariable("user-information-id") userInformationId: UUID
    ) : ResponseEntity<UserInformationResponseDto> {
        val responseDto = userInformationService.setActiveCalendarConnectionType(userInformationId, null)
            ?.toResponseDto() ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(responseDto)
    }

    @PatchMapping("tesla-connections/deactivate")
    fun setActiveTeslaConnectionToNull(
        @PathVariable("user-information-id") userInformationId: UUID
    ) : ResponseEntity<UserInformationResponseDto> {
        val responseDto = userInformationService.setActiveTeslaConnectionType(userInformationId, null)
            ?.toResponseDto() ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(responseDto)
    }
}