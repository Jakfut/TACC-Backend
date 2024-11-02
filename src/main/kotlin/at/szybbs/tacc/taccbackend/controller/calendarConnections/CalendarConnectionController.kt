package at.szybbs.tacc.taccbackend.controller.calendarConnections

import at.szybbs.tacc.taccbackend.dto.calendarConnections.CalendarConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.calendarConnections.CalendarConnectionResponseDto
import at.szybbs.tacc.taccbackend.dto.calendarConnections.CalendarConnectionUpdateDto
import org.springframework.http.ResponseEntity
import java.util.UUID

/**
 * Controller interface for managing calendar connections in the system.
 *
 * This interface defines methods for retrieving, creating, updating,
 * and deleting calendar connections associated with user information.
 *
 * @param R Type of the creation DTO for calendar connections.
 * @param E Type of the response DTO for calendar connections.
 * @param U Type of the update DTO for calendar connections.
 */
interface CalendarConnectionController<R: CalendarConnectionCreationDto, E: CalendarConnectionResponseDto, U: CalendarConnectionUpdateDto> {
    fun getCalendarConnection(userInformationId: UUID) : ResponseEntity<E>
    fun createCalendarConnection(userInformationId: UUID, creationDto: R) : ResponseEntity<E>
    fun updateCalendarConnection(userInformationId: UUID, updateDto: U) : ResponseEntity<E>
    fun deleteCalendarConnection(userInformationId: UUID) : ResponseEntity<Void>
}