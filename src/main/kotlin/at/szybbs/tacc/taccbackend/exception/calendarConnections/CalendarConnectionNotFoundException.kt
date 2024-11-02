package at.szybbs.tacc.taccbackend.exception.calendarConnections

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import java.util.UUID

/**
 * Exception thrown when a calendar connection is not found for a given user and type.
 *
 * @param calendarType The type of the calendar connection that was not found.
 * @param userInformationId The ID of the user associated with the calendar connection.
 */
class CalendarConnectionNotFoundException(calendarType: CalendarType, userInformationId: UUID) :
    RuntimeException("No CalendarConnection found with type $calendarType for UserInformation id $userInformationId") {
}