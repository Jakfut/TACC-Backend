package at.szybbs.tacc.taccbackend.exception.calendarConnection

import java.util.UUID

/**
 * Exception thrown when no active calendar connection is found for a user.
 *
 * @param userInformationId The UUID of the user whose calendar connection is missing.
 */
class ActiveCalendarConnectionNotFoundException(userInformationId: UUID) :
    RuntimeException("No active calendar connection found for userInformation id $userInformationId.") {
}