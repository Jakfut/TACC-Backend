package at.szybbs.tacc.taccbackend.exception.calendarConnection

import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarConnectionId

class CalendarConnectionAlreadyExistsException(calendarConnectionId: CalendarConnectionId) :
    RuntimeException("CalendarConnection of type ${calendarConnectionId.type} with userInformation id ${calendarConnectionId.userInformationId} already exists.") {
}