package at.szybbs.tacc.taccbackend.repository.calendarConnection

import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarConnection
import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarConnectionId
import org.springframework.data.repository.CrudRepository
import java.util.UUID

/**
 * Repository for CRUD operations on [CalendarConnection] entities in the database.
 */
interface CalendarConnectionRepository : CrudRepository<CalendarConnection, CalendarConnectionId> {
    fun findByUserInformationId(uuid: UUID): List<CalendarConnection>

    fun findByUserInformationIdAndActiveIsTrue(uuid: UUID): List<CalendarConnection>
}