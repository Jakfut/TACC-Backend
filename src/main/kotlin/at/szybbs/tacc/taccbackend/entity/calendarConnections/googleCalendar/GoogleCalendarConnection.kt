package at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar

import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionResponseDto
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarConnection
import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.UUID

@Entity
@EntityListeners(GoogleCalendarConnectionListener::class)
@Table(name = "google_calendar_connection")
data class GoogleCalendarConnection(

    @Id
    @Column(name = "user_information_id", updatable = false, nullable = false)
    override var userInformationId: UUID,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_information_id", insertable=false, updatable=false)
    @JsonIgnore
    override var userInformation: UserInformation? = null,

    @Column(name = "access_token")
    var accessToken: String? = null,

    @Column(name = "refresh_token")
    var refreshToken: String? = null,

    @Column(name = "email")
    var email: String? = null,

    @Column(name = "keyword")
    var keyword: String? = null,
) : CalendarConnection {
    override fun toResponseDto(): GoogleCalendarConnectionResponseDto {
        return GoogleCalendarConnectionResponseDto(
            userInformationId = this.userInformationId,
            email = this.email,
            keyword = this.keyword
        )
    }
}

