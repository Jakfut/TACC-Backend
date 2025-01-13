package at.szybbs.tacc.taccbackend.exception.userInformation

import java.util.UUID

/**
 * Exception thrown when a user can't be deleted from the keycloak service.
 * This may happen if e.g. the service is down.
 *
 * @param userInformationId The ID of the user information.
 */
class UserInformationKeycloakDeletionSynchronizationException(userInformationId: UUID) :
    RuntimeException("User information with id $userInformationId could not be deleted due to a synchronization error.") {
}