package at.szybbs.tacc.taccbackend.model.teslaConnection

import at.szybbs.tacc.taccbackend.model.userInformation.UserInformation
import com.fasterxml.jackson.databind.JsonNode
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

/**
 * Represents a connection to an external TeslaConnection service provider for a userInformation.
 *
 * This class captures the details of a userInformation's TeslaConnection connection, including its status (active or not),
 * the configuration needed to connect to the TeslaConnection service, and establishes a relationship with the userInformation
 * information. The connection is uniquely identified by a composite primary key consisting of the userInformation's UUID
 * and the TeslaConnection connection provider type. This means that a userInformation can have only one connection for each external service provider.
 */

@Entity
@Table(name = "tesla_connection")
data class TeslaConnection(
    /**
     * Composite primary key consisting of the userInformation's UUID and the type of TeslaConnection provider.
     */
    @EmbeddedId
    var id: TeslaConnectionId,

    /**
     * Many-to-One relationship with the UserInformation entity, mapped by the "user_information_id" column,
     * which is part of the composite primary key. This field is not insertable or updatable to avoid conflicts
     * with the "user_information_id" in the [TeslaConnectionId] object.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_information_id", insertable=false, updatable=false)
    var userInformation: UserInformation? = null,

    /**
     * Indicates whether this TeslaConnection connection is currently active.
     */
    @Column(name = "active", nullable = false)
    var active: Boolean,

    /**
     * Configuration for connecting to the external TeslaConnection service provider.
     * The @JdbcTypeCode(SqlTypes.JSON) annotation enables automatic mapping to jsonb in Postgres and vice versa.
     */
    @Column(name = "config", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    var config: JsonNode
)
