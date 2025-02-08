package at.szybbs.tacc.taccbackend.validation.calendarConnections

import at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar.GoogleCalendarConnection
import at.szybbs.tacc.taccbackend.exception.calendarConnections.CalendarConnectionValidationException
import org.springframework.stereotype.Component

/**
 * Validator for Google Calendar connections.
 *
 * This component validates the fields of a Google Calendar connection entity,
 * ensuring that they conform to the specified constraints.
 */
@Component
class GoogleCalendarConnectionValidator {
    fun validate(googleCalendarConnection: GoogleCalendarConnection) {
        val keywordStart = googleCalendarConnection.keywordStart
        if (!keywordStart.matches(Regex("^#[a-zA-Z0-9]{3,5}\$"))) {
            throw CalendarConnectionValidationException("keywordStart", keywordStart)
        }

        val keywordEnd = googleCalendarConnection.keywordEnd
        if (!keywordEnd.matches(Regex("^#[a-zA-Z0-9]{3,5}\$"))) {
            throw CalendarConnectionValidationException("keywordEnd", keywordEnd)
        }
    }
}