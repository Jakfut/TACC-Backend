package at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar

import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionResponseDto
import at.szybbs.tacc.taccbackend.encryption.AttributeEncryptor
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

    @Convert(converter = AttributeEncryptor::class)
    @Column(name = "access_token", columnDefinition = "TEXT")
    var accessToken: String? = null,

    @Convert(converter = AttributeEncryptor::class)
    @Column(name = "refresh_token", columnDefinition = "TEXT")
    var refreshToken: String? = null,

    @Column(name = "email", columnDefinition = "TEXT")
    var email: String? = null,

    @Column(name = "keyword", nullable = false)
    var keyword: String,

) : CalendarConnection {
    override fun toResponseDto(): GoogleCalendarConnectionResponseDto {
        return GoogleCalendarConnectionResponseDto(
            userInformationId = this.userInformationId,
            email = this.email,
            keyword = this.keyword
        )
    }
}

