package at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar

import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionResponseDto
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarConnection
import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.UUID

@Entity
@EntityListeners(GoogleCalendarConnectionListener::class)
@Table(name = "google_calendar_connection")
data class GoogleCalendarConnection(

    @Id
    @Column(name = "user_information_id", updatable = false, nullable = false)
    override var userInformationId: UUID,

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId("userInformationId")
    @JoinColumn(name = "user_information_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    override var userInformation: UserInformation? = null,

    @Column(name= "oauth2_connection_id", unique = true)
    val oauth2ConnectionId: String = UUID.randomUUID().toString(),

    @Column(name = "email")
    var email: String? = null,

    @Column(name = "keywordStart", nullable = false)
    var keywordStart: String,

    @Column(name = "keywordEnd", nullable = false)
    var keywordEnd: String,
) : CalendarConnection {
    override fun toResponseDto(): GoogleCalendarConnectionResponseDto {
        return GoogleCalendarConnectionResponseDto(
            userInformationId = this.userInformationId,
            email = this.email,
            keywordStart = this.keywordStart,
            keywordEnd = this.keywordEnd,
        )
    }
}

