package at.szybbs.tacc.taccbackend.controller.userInformation

import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationCreationDto
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationResponseDto
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationUpdateDefaultValuesDto
import at.szybbs.tacc.taccbackend.service.userInformation.UserInformationService
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

    @PostMapping
    fun createUserInformation(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody creationDto: UserInformationCreationDto
    ) : ResponseEntity<UserInformationResponseDto> {
        val responseDto = userInformationService.createUserInformation(userInformationId, creationDto, "email@email.com")
            .toResponseDto()

        // TODO: replace jwtEmail

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
            .toResponseDto()

        return ResponseEntity.ok(responseDto)
    }
}