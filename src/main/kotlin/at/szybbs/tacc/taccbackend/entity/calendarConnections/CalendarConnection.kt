package at.szybbs.tacc.taccbackend.entity.calendarConnections

import at.szybbs.tacc.taccbackend.dto.calendarConnections.CalendarConnectionResponseDto
import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import java.util.*

interface CalendarConnection {
    var userInformationId: UUID
    var userInformation: UserInformation?

    fun toResponseDto() : CalendarConnectionResponseDto
}