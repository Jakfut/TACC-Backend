package at.szybbs.tacc.taccbackend.validation.teslaConnections

import at.szybbs.tacc.taccbackend.entity.teslaConnections.tessie.TessieConnection
import org.springframework.stereotype.Component

/**
 * Validator for Tessie connections.
 *
 * This component validates the fields of a Tessie connection entity,
 * ensuring that they conform to the specified constraints.
 *
 * Validation is currently empty, because if the connection is valid, is tested by
 * actually messaging the Tesla in the Service layer.
 */
@Component
class TessieConnectionValidator {
    fun validate(tessieConnection: TessieConnection) {
        // TODO: ping Tesla using http client
    }
}