package at.szybbs.tacc.taccbackend.controller

import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationCreationDto
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationResponseDto
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationUpdateDefaultValuesDto
import at.szybbs.tacc.taccbackend.service.UserInformationService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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

    @PreAuthorize("hasAuthority('SCOPE_' + @environment.getProperty('security.authentication.roles.create-user'))")
    @PostMapping
    fun createUserInformation(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody creationDto: UserInformationCreationDto
    ) : ResponseEntity<UserInformationResponseDto> {
        val responseDto = userInformationService.createUserInformation(
            userInformationId,
            creationDto
        ).toResponseDto()

        return ResponseEntity.ok(responseDto)
    }

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
        val responseDto = userInformationService.setActiveCalendarConnectionTypeToNull(userInformationId)
            ?.toResponseDto() ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(responseDto)
    }

    @PatchMapping("tesla-connections/deactivate")
    fun setActiveTeslaConnectionToNull(
        @PathVariable("user-information-id") userInformationId: UUID
    ) : ResponseEntity<UserInformationResponseDto> {
        val responseDto = userInformationService.setActiveTeslaConnectionTypeToNull(userInformationId)
            ?.toResponseDto() ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(responseDto)
    }

    @PatchMapping("/oauth2-session")
    fun setOauth2Session(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody oauth2Session: String
    ) : ResponseEntity<UserInformationResponseDto> {
        val responseDto = userInformationService.setOauth2Session(userInformationId, oauth2Session)
            .toResponseDto()

        return ResponseEntity.ok(responseDto)
    }
}