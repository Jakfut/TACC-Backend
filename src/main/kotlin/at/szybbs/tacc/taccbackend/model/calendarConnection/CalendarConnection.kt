package at.szybbs.tacc.taccbackend.model.calendarConnection

import at.szybbs.tacc.taccbackend.model.userInformation.UserInformation
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.JsonNode
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

/**
 * Represents a connection to an external calendar service provider for a userInformation.
 *
 * This class captures the details of a userInformation's calendar connection, including its status (active or not),
 * the configuration needed to connect to the calendar service, and establishes a relationship with the userInformation
 * information. The connection is uniquely identified by a composite primary key consisting of the UUID of the userInformation
 * and the calendar connection provider type. This means that a userInformation can have only one connection for each external service provider.
 */

@Entity
@Table(name = "calendar_connection")
data class CalendarConnection(

    /**
     * Composite primary key consisting of the UUID of the userInformation and the type of calendar connection (external service provider).
     */
    @EmbeddedId
    var id: CalendarConnectionId,

    /**
     * Many-to-One relationship with the UserInformation entity, mapped by the "user_information_id" column,
     * which is part of the composite primary key. This field is not insertable or updatable to avoid conflicts
     * with the "user_information_id" in the [CalendarConnectionId] object.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_information_id", insertable=false, updatable=false)
    @JsonIgnore
    val userInformation: UserInformation?,

    /**
     * Indicates whether this calendar connection is currently active.
     */
    @Column(name = "active", nullable = false)
    var active: Boolean,

    /**
     * Configuration for connecting to the external calendar service provider.
     * The @JdbcTypeCode(SqlTypes.JSON) annotation enables automatic mapping to jsonb in Postgres and vice versa.
     */
    @Column(name = "config", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    var config: JsonNode
)

