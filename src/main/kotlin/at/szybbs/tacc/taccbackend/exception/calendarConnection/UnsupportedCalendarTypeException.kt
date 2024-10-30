package at.szybbs.tacc.taccbackend.exception.calendarConnection

import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarType

/**
 * Exception thrown when an unsupported CalendarType is used for a specific action.
 *
 * @param calendarType The type of the calendar that is unsupported.
 * @param action The action that was attempted with the unsupported CalendarType.
 */
class UnsupportedCalendarTypeException(calendarType: CalendarType, action: String) :
    RuntimeException("Unsupported CalendarType '${calendarType.name}' for action '$action'.") {
}