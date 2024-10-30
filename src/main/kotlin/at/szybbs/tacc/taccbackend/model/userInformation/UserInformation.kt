package at.szybbs.tacc.taccbackend.model.userInformation

import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarConnection
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnection
import jakarta.persistence.*
import java.util.*

/**
 * Represents the userInformation information in the system.
 *
 * This class holds details about the userInformation, including their unique ID, email, and settings
 * for travel time and climate control. It also establishes one-to-many relationships
 * with [CalendarConnection] and [TeslaConnection] entities, allowing for the management of
 * external connections associated with the userInformation.
 */

@Entity
@Table(name = "user_information")
data class UserInformation(
    @Id
    @Column(name = "id", nullable = false)
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
     * Represents the one-to-many relationships of this userInformation with [CalendarConnection] and [TeslaConnection] entities.
     *
     * Both collections are lazily fetched, and all persistence operations (cascade) will propagate to the child entities.
     * Orphan removal is enabled, meaning that any removed connection from these lists will also be deleted from the database.
     */

    @OneToMany(mappedBy = "userInformation", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var calendarConnections: List<CalendarConnection> = ArrayList(),

    @OneToMany(mappedBy = "userInformation", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var teslaConnections: List<TeslaConnection> = ArrayList(),
)
