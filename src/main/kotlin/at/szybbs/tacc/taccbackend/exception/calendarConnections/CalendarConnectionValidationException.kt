package at.szybbs.tacc.taccbackend.exception.calendarConnections

/**
 * Exception thrown when a validation error occurs for a calendar connection field.
 *
 * @param fieldName The name of the field that failed validation.
 * @param fieldValue The invalid value that was provided for the field.
 */
class CalendarConnectionValidationException(fieldName: String, fieldValue: String) :
    RuntimeException("Invalid value for CalendarConnection field '$fieldName': '$fieldValue'.") {
}