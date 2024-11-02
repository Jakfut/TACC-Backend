package at.szybbs.tacc.taccbackend.exception.calendarConnections

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import java.util.UUID

/**
 * Exception thrown when a calendar connection already exists for a given user and type.
 *
 * @param calendarType The type of the calendar connection that already exists.
 * @param userInformationId The ID of the user associated with the calendar connection.
 */
class CalendarConnectionAlreadyExistsException(calendarType: CalendarType, userInformationId: UUID) :
    RuntimeException("CalendarConnection of type $calendarType with userInformation id $userInformationId already exists.") {
}