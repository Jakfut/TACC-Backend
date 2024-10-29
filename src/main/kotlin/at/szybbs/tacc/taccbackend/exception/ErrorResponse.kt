package at.szybbs.tacc.taccbackend.exception

/**
 * Represents a standardized error response structure.
 *
 * @param timestamp The time when the error occurred, formatted as a String.
 * @param status The HTTP status code associated with the error.
 * @param error A short description of the error.
 * @param trace A stack trace of the exception for debugging purposes.
 * @param message A detailed message describing the error.
 * @param path The request URI where the error occurred.
 */
data class ErrorResponse(
    val timestamp: String,
    val status: Int,
    val error: String,
    val trace: String,
    val message: String,
    val path: String
)
