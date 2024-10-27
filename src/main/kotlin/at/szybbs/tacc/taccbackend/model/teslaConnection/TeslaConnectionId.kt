package at.szybbs.tacc.taccbackend.model.teslaConnection

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.util.*

/**
 * Represents the composite primary key for a [TeslaConnection] entity.
 */

@Embeddable
data class TeslaConnectionId(
    /**
     * Type of the external TeslaConnection service provider.
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    var type: TeslaConnectionType,

    /**
     * UUID of the UserInformation entity, used for the Many-To-One relationship in the [TeslaConnection] object.
     */
    @Column(name = "user_information_id", nullable = false)
    var userInformationId: UUID,
)
