package at.szybbs.tacc.taccbackend.exception.teslaConnection

import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnectionId

/**
 * Exception thrown when a specified TeslaConnection cannot be found.
 *
 * @param teslaConnectionId The identifier of the TeslaConnection that was not found, containing its type and associated user information ID.
 */
class TeslaConnectionNotFoundException(teslaConnectionId: TeslaConnectionId) :
    RuntimeException("TeslaConnection of type ${teslaConnectionId.type} with userInformation id ${teslaConnectionId.userInformationId} not found.") {
}