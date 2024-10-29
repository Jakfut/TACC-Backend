package at.szybbs.tacc.taccbackend.exception.calendarConnection

import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarType

/**
 * Exception thrown when an unsupported calendar type is used for a specific action.
 *
 * @param calendarType The type of the calendar that is unsupported.
 * @param action The action that was attempted with the unsupported calendar type.
 */
class UnsupportedCalendarTypeException(calendarType: CalendarType, action: String) :
    RuntimeException("Unsupported calendar type '${calendarType.name}' for action '$action'.") {
}