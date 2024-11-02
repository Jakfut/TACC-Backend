package at.szybbs.tacc.taccbackend.exception.teslaConnections

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import java.util.UUID

/**
 * Exception thrown when a Tesla connection is not found for a given user and type.
 *
 * @param teslaConnectionType The type of the Tesla connection that was not found.
 * @param userInformationId The ID of the user associated with the Tesla connection.
 */
class TeslaConnectionNotFoundException(teslaConnectionType: TeslaConnectionType, userInformationId: UUID) :
    RuntimeException("No TeslaConnection found with type $teslaConnectionType for UserInformation id $userInformationId") {
}