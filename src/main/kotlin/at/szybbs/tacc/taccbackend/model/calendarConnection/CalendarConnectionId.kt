package at.szybbs.tacc.taccbackend.model.calendarConnection

import jakarta.persistence.*
import java.util.UUID

/**
 * Represents the composite primary key for a [CalendarConnection] entity.
 */

@Embeddable
data class CalendarConnectionId(
    /**
     * Type of the external calendar service provider.
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    var type: CalendarType,

    /**
     * UUID of the UserInformation entity, used for the Many-To-One relationship in the [CalendarConnection] object.
     */
    @Column(name = "user_information_id", nullable = false)
    var userInformationId: UUID,
)