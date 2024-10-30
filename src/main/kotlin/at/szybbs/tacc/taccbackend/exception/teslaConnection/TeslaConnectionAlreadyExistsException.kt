package at.szybbs.tacc.taccbackend.exception.teslaConnection

import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnectionId

class TeslaConnectionAlreadyExistsException(teslaConnectionId: TeslaConnectionId) :
    RuntimeException("TeslaConnection of type ${teslaConnectionId.type} with userInformation id ${teslaConnectionId.userInformationId} already exists.") {
}