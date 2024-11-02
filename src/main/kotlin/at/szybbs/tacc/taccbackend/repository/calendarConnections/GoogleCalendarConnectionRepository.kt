package at.szybbs.tacc.taccbackend.repository.calendarConnections

import at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar.GoogleCalendarConnection
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Repository for CRUD operations on [GoogleCalendarConnection] entities in the database.
 */
interface GoogleCalendarConnectionRepository : JpaRepository<GoogleCalendarConnection, UUID> {

}