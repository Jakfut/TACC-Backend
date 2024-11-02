package at.szybbs.tacc.taccbackend.exception.teslaConnections

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import java.util.UUID

/**
 * Exception thrown when a Tesla connection already exists for a given user and type.
 *
 * @param teslaConnectionType The type of the Tesla connection that already exists.
 * @param userInformationId The ID of the user associated with the Tesla connection.
 */
class TeslaConnectionAlreadyExistsException(teslaConnectionType: TeslaConnectionType, userInformationId: UUID) :
    RuntimeException("TeslaConnection of type $teslaConnectionType with userInformation id $userInformationId already exists.") {
}