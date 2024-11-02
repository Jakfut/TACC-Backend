package at.szybbs.tacc.taccbackend.entity.userInformation

import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationResponseDto
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import jakarta.persistence.*
import java.util.*

@Entity
@EntityListeners(UserInformationListener::class)
@Table(name = "user_information")
data class UserInformation(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID,

    @Column(name = "email", nullable = false)
    var email: String,

    /**
     * Value in minutes that serves as a fallback travel time if no destination is provided for an appointment.
     */
    @Column(name = "no_dest", nullable = false)
    var noDestMinutes: Int = 0,

    /**
     * Value in minutes that determines the duration of the climate control runtime before departure.
     */
    @Column(name = "cc_runtime", nullable = false)
    var ccRuntimeMinutes: Int = 0,

    /**
     * Value in minutes that specifies how early the userInformation wants to arrive at the destination before the appointment starts.
     */
    @Column(name = "arrival_buffer", nullable = false)
    var arrivalBufferMinutes: Int = 0,

    /**
     * The type of the active calendar connection.
     * This property can be null if there is no active connection type.
     */
    @Column(name = "active_calendar_connection_type")
    var activeCalendarConnectionType: CalendarType? = null,

    /**
     * The type of the active Tesla connection.
     * This property can be null if there is no active connection type.
     */
    @Column(name = "active_tesla_connection_type")
    var activeTeslaConnectionType: TeslaConnectionType? = null,
) {
    fun toResponseDto() : UserInformationResponseDto {
        return UserInformationResponseDto(
            id = id,
            email = email,
            noDestMinutes = noDestMinutes,
            ccRuntimeMinutes = ccRuntimeMinutes,
            arrivalBufferMinutes = arrivalBufferMinutes,
            activeCalendarConnectionType = activeCalendarConnectionType,
            activeTeslaConnectionType = activeTeslaConnectionType
        )
    }
}
