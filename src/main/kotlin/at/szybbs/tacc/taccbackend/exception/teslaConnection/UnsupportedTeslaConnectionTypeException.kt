package at.szybbs.tacc.taccbackend.exception.teslaConnection

import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnectionType

/**
 * Exception thrown when an unsupported TeslaConnectionTye is used for a specific action.
 *
 * @param teslaConnectionType The type of the TeslaConnection that is unsupported.
 * @param action The action that was attempted with the unsupported TeslaConnectionType.
 */
class UnsupportedTeslaConnectionTypeException(teslaConnectionType: TeslaConnectionType, action: String) :
    RuntimeException("Unsupported TeslaConnectionType '${teslaConnectionType.name}' for action '$action'.") {
}