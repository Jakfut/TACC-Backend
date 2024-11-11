package at.szybbs.tacc.taccbackend.controller.calendarConnections

import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionResponseDto
import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionUpdateDto
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationResponseDto
import at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar.GoogleCalendarConnection
import at.szybbs.tacc.taccbackend.service.calendarConnections.GoogleCalendarConnectionService
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
 * Controller for managing Google Calendar connections associated with user information.
 *
 * This REST controller provides endpoints for creating, retrieving, updating, and deleting
 * Google Calendar connections for a specified user.
 *
 * @property googleCalendarConnectionService Service for managing Google Calendar connections.
 */
@RestController
@RequestMapping("/api/user/{user-information-id}/calendar-connections/google-calendar")
class GoogleCalendarConnectionController (
    private val googleCalendarConnectionService: GoogleCalendarConnectionService
) {

    @GetMapping
    fun getCalendarConnection(
        @PathVariable("user-information-id") userInformationId: UUID,
    ) : ResponseEntity<GoogleCalendarConnectionResponseDto> {
        val responseDto = googleCalendarConnectionService.getCalendarConnection(userInformationId)
            .toResponseDto()

        return ResponseEntity.ok(responseDto)
    }

    @DeleteMapping
    fun deleteCalendarConnection(
        @PathVariable("user-information-id") userInformationId: UUID
    ): ResponseEntity<Void> {
        googleCalendarConnectionService.deleteCalendarConnection(userInformationId)

        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/active")
    fun setCalendarConnectionToActive(
        @PathVariable("user-information-id") userInformationId: UUID
    ): ResponseEntity<UserInformationResponseDto> {
        val responseDto = googleCalendarConnectionService.setCalendarConnectionToActive(userInformationId)
            ?.toResponseDto() ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(responseDto)
    }

    @PatchMapping
    fun updateCalendarConnectionPublicFields(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody updateDto: GoogleCalendarConnectionUpdateDto
    ): ResponseEntity<GoogleCalendarConnectionResponseDto> {
        val responseDto = googleCalendarConnectionService.updateCalendarConnectionPublicFields(userInformationId, updateDto)
            ?.toResponseDto() ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(responseDto)
    }

    @PostMapping
    fun createCalendarConnection(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody creationDto: GoogleCalendarConnectionCreationDto
    ): ResponseEntity<GoogleCalendarConnectionResponseDto> {
        val responseDto = googleCalendarConnectionService.createCalendarConnection(userInformationId, creationDto)
            .toResponseDto()

        return ResponseEntity.ok(responseDto)
    }
}