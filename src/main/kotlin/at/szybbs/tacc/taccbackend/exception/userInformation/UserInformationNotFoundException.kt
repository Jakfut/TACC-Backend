package at.szybbs.tacc.taccbackend.exception.userInformation

import java.util.*

/**
 * Exception thrown when user information cannot be found for a specified ID.
 *
 * @param userInformationId The UUID of the user information that was not found.
 */
class UserInformationNotFoundException(userInformationId: UUID) :
    RuntimeException("UserInformation with id $userInformationId not found.") {
}