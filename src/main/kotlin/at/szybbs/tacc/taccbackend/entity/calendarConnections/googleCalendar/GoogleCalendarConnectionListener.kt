package at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar

import at.szybbs.tacc.taccbackend.validation.calendarConnections.GoogleCalendarConnectionValidator
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import org.springframework.stereotype.Component

@Component
class GoogleCalendarConnectionListener (
    private val validator: GoogleCalendarConnectionValidator
) {

    @PrePersist
    @PreUpdate
    fun validate(googleCalendarConnection: GoogleCalendarConnection) {
        validator.validate(googleCalendarConnection)
    }

    /**
     * TODO: implement encryption/decryption
     */
}