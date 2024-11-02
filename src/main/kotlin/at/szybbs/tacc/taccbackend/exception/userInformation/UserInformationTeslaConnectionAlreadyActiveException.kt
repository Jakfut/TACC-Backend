package at.szybbs.tacc.taccbackend.exception.userInformation

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import java.util.UUID

/**
 * Exception thrown when a Tesla connection of the specified type is
 * already active for a UserInformation.
 *
 * @param userInformationId The UUID of the UserInformation with the
 *                          active calendar connection.
 * @param teslaConnectionType The type of the already active Tesla connection.
 */
class UserInformationTeslaConnectionAlreadyActiveException (userInformationId: UUID, teslaConnectionType: TeslaConnectionType)
    : RuntimeException("UserInformation, with id $userInformationId, active Tesla connection is already of type $teslaConnectionType.")