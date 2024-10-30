package at.szybbs.tacc.taccbackend.exception.teslaConnection

import java.util.UUID

/**
 * Exception thrown when no active calendar connection is found for a user.
 *
 * @param userInformationId The UUID of the user whose calendar connection is missing.
 */
class ActiveTeslaConnectionNotFoundException(userInformationId: UUID) :
    RuntimeException("No active TeslaConnection found for userInformation id $userInformationId.") {
}