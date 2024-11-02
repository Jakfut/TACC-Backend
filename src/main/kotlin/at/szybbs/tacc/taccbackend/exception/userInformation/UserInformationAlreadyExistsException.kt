package at.szybbs.tacc.taccbackend.exception.userInformation

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import java.util.UUID

/**
 * Exception thrown when a user information already exists.
 *
 * @param userInformationId The ID of the user information.
 */
class UserInformationAlreadyExistsException(userInformationId: UUID) :
    RuntimeException("User information with id $userInformationId already exists.") {
}