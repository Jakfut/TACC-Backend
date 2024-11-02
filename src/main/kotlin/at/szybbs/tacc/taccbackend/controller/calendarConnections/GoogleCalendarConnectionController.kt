package at.szybbs.tacc.taccbackend.controller.calendarConnections

import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionResponseDto
import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionUpdateDto
import at.szybbs.tacc.taccbackend.service.calendarConnections.GoogleCalendarConnectionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
@RequestMapping("/api/user/{user-information-id}/calendar-connection/google-calendar")
class GoogleCalendarConnectionController (
    private val googleCalendarConnectionService: GoogleCalendarConnectionService
) : CalendarConnectionController<
        GoogleCalendarConnectionCreationDto,
        GoogleCalendarConnectionResponseDto,
        GoogleCalendarConnectionUpdateDto> {

    @GetMapping
    override fun getCalendarConnection(
        @PathVariable("user-information-id") userInformationId: UUID,
    ) : ResponseEntity<GoogleCalendarConnectionResponseDto> {
        val responseDto = googleCalendarConnectionService.getCalendarConnection(userInformationId)
            .toResponseDto()

        return ResponseEntity.ok(responseDto)
    }

    @DeleteMapping
    override fun deleteCalendarConnection(
        @PathVariable("user-information-id") userInformationId: UUID
    ): ResponseEntity<Void> {
        googleCalendarConnectionService.deleteCalendarConnection(userInformationId)

        return ResponseEntity.noContent().build()
    }

    @PutMapping
    override fun updateCalendarConnection(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody updateDto: GoogleCalendarConnectionUpdateDto
    ): ResponseEntity<GoogleCalendarConnectionResponseDto> {
        val responseDto = googleCalendarConnectionService.updateCalendarConnection(userInformationId, updateDto)
            .toResponseDto()

        return ResponseEntity.ok(responseDto)
    }

    @PostMapping
    override fun createCalendarConnection(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody creationDto: GoogleCalendarConnectionCreationDto
    ): ResponseEntity<GoogleCalendarConnectionResponseDto> {
        val responseDto = googleCalendarConnectionService.createCalendarConnection(userInformationId, creationDto)
            .toResponseDto()

        return ResponseEntity.ok(responseDto)
    }
}