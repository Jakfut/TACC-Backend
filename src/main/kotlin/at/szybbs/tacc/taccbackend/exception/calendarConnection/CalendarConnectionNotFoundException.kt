package at.szybbs.tacc.taccbackend.exception.calendarConnection

import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarConnectionId

/**
 * Exception thrown when a specified calendar connection cannot be found.
 *
 * @param calendarConnectionId The identifier of the calendar connection that was not found, containing its type and associated user information ID.
 */
class CalendarConnectionNotFoundException(calendarConnectionId: CalendarConnectionId) :
    RuntimeException("CalendarConnection of type ${calendarConnectionId.type} with userInformation id ${calendarConnectionId.userInformationId} not found.") {
}