package at.szybbs.tacc.taccbackend.exception.userInformation

/**
 * Exception thrown when a validation error occurs for a user information field.
 *
 * @param fieldName The name of the field that failed validation.
 * @param fieldValue The invalid value that was provided for the field.
 */
class UserInformationValidationException(fieldName: String, fieldValue: String) :
    RuntimeException("Invalid value for UserInformation field '$fieldName': '$fieldValue'.") {
}