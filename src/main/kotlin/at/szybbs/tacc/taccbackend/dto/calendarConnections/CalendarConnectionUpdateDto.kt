package at.szybbs.tacc.taccbackend.dto.calendarConnections

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarConnection

/**
 * Marker interface for DTOs used to update calendar connections.
 * This can be extended in the future if properties are needed.
 */
interface CalendarConnectionUpdateDto<T: CalendarConnection> {
    fun hasChanged(calendarConnection: T): Boolean
}