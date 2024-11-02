package at.szybbs.tacc.taccbackend.entity.teslaConnections

import at.szybbs.tacc.taccbackend.dto.teslaConnections.TeslaConnectionResponseDto
import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import java.util.*

interface TeslaConnection {
    var userInformationId: UUID
    var userInformation: UserInformation?

    fun toResponseDto() : TeslaConnectionResponseDto
}