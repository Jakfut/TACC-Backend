package at.szybbs.tacc.taccbackend.exception.userInformation

/**
 * Exception thrown when a UserInformation field has an unexpected state.
 *
 * @param fieldName The name of the field that has an unexpected state.
 * @param fieldValue The unexpected value of the field.
 */
class UserInformationUnexpectedStateException (fieldName: String, fieldValue: String)
    : RuntimeException("Unexpected state for field '$fieldName': '$fieldValue'.")