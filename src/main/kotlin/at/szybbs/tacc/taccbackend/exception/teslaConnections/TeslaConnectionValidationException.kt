package at.szybbs.tacc.taccbackend.exception.teslaConnections

/**
 * Exception thrown when a validation error occurs for a Tesla connection field.
 *
 * @param fieldName The name of the field that failed validation.
 * @param fieldValue The invalid value that was provided for the field.
 */
class TeslaConnectionValidationException(fieldName: String, fieldValue: String) :
    RuntimeException("Invalid value for TeslaConnection field '$fieldName': '$fieldValue'.") {
}