package at.szybbs.tacc.taccbackend.exception.userInformation

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import java.util.UUID

/**
 * Exception thrown when a calendar connection of the specified type is
 * already active for a UserInformation.
 *
 * @param userInformationId The UUID of the UserInformation with the
 *                          active calendar connection.
 * @param calendarType The type of the already active calendar connection.
 */
class UserInformationCalendarConnectionAlreadyActiveException (userInformationId: UUID ,calendarType: CalendarType)
    : RuntimeException("UserInformation, with id $userInformationId, active calendar connection is already of type $calendarType.")