package at.szybbs.tacc.taccbackend.repository.teslaConnection

import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarConnection
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnection
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnectionId
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Repository for CRUD operations on [TeslaConnection] entities in the database.
 */
interface TeslaConnectionRepository : JpaRepository<TeslaConnection, TeslaConnectionId> {
    fun findByUserInformationId(uuid: UUID): List<TeslaConnection>

    fun findByUserInformationIdAndActiveIsTrue(uuid: UUID): List<TeslaConnection>
}