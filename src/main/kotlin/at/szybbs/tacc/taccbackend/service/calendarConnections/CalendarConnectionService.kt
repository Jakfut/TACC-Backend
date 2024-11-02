package at.szybbs.tacc.taccbackend.service.calendarConnections

import at.szybbs.tacc.taccbackend.dto.calendarConnections.CalendarConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.calendarConnections.CalendarConnectionUpdateDto
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarConnection
import java.util.UUID

/**
 * Service interface for managing calendar connections in the system.
 *
 * @param T Type of the calendar connection entity, used as the return value for retrieval and creation methods.
 * @param R Type of the creation DTO for calendar connections.
 * @param U Type of the update DTO for calendar connections.
 */
interface CalendarConnectionService<T: CalendarConnection, R: CalendarConnectionCreationDto, U: CalendarConnectionUpdateDto> {
    fun getCalendarConnection(userInformationId: UUID) : T
    fun createCalendarConnection(userInformationId: UUID, creationDto: R) : T
    fun updateCalendarConnection(userInformationId: UUID, updateDto: U) : T
    fun deleteCalendarConnection(userInformationId: UUID)
    fun calendarConnectionExists(userInformationId: UUID): Boolean
}