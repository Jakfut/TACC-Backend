package at.szybbs.tacc.taccbackend.exception

import at.szybbs.tacc.taccbackend.exception.calendarConnections.CalendarConnectionAlreadyExistsException
import at.szybbs.tacc.taccbackend.exception.calendarConnections.CalendarConnectionNotFoundException
import at.szybbs.tacc.taccbackend.exception.calendarConnections.CalendarConnectionValidationException
import at.szybbs.tacc.taccbackend.exception.teslaConnections.TeslaConnectionAlreadyExistsException
import at.szybbs.tacc.taccbackend.exception.teslaConnections.TeslaConnectionNotFoundException
import at.szybbs.tacc.taccbackend.exception.teslaConnections.TeslaConnectionValidationException
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime

/**
 * Global exception handler for controller-level exceptions.
 *
 * This class uses Spring's @RestControllerAdvice to handle specific exceptions
 * thrown in the application and return appropriate HTTP response statuses and messages.
 */
@RestControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(
        CalendarConnectionNotFoundException::class,
        UserInformationNotFoundException::class,
        TeslaConnectionNotFoundException::class,
    )
    fun handleCustomResourceNotFoundException(e: RuntimeException): ResponseEntity<ErrorResponse> {
        return createErrorResponse(e = e, status = HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(
        CalendarConnectionAlreadyExistsException::class,
        TeslaConnectionAlreadyExistsException::class,
    )
    fun handleConflictExceptions(e: RuntimeException): ResponseEntity<ErrorResponse> {
        return createErrorResponse(e = e, status = HttpStatus.CONFLICT)
    }

    @ExceptionHandler(
        CalendarConnectionValidationException::class,
        TeslaConnectionValidationException::class,
    )
    fun handleCustomBadRequestException(e: RuntimeException): ResponseEntity<ErrorResponse> {
        return createErrorResponse(e = e, status = HttpStatus.BAD_REQUEST)
    }

    /**
     * Creates a standardized error response for exceptions.
     *
     * @param e The RuntimeException that was thrown.
     * @param status The HTTP status to associate with the error response.
     * @return A ResponseEntity containing the ErrorResponse with the specified status and details.
     */
    private fun createErrorResponse(e: RuntimeException, status: HttpStatus): ResponseEntity<ErrorResponse> {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        val request = requestAttributes.request

        // Create an error response
        return ResponseEntity.status(status).body(
            ErrorResponse(
                status = status.value(),
                error = status.reasonPhrase,
                trace = e.stackTraceToString(),
                message = e.message ?: "An unexpected error occurred",
                timestamp = LocalDateTime.now().toString(),
                path = request.requestURI
        )
        )
    }
}