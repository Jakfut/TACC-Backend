package at.szybbs.tacc.taccbackend.repository.teslaConnections

import at.szybbs.tacc.taccbackend.entity.teslaConnections.tessie.TessieConnection
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Repository for CRUD operations on [TessieConnection] entities in the database.
 */
interface TessieConnectionRepository : JpaRepository<TessieConnection, UUID> {

}