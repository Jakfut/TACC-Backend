package at.szybbs.tacc.taccbackend.repository.teslaConnection

import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnection
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnectionId
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repository for CRUD operations on [TeslaConnection] entities in the database.
 */
interface TeslaConnectionRepository : JpaRepository<TeslaConnection, TeslaConnectionId> {
}