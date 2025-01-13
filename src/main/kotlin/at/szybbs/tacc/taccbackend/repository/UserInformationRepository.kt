package at.szybbs.tacc.taccbackend.repository

import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Repository for CRUD operations on [UserInformation] entities in the database.
 */
interface UserInformationRepository : JpaRepository<UserInformation, UUID> {
    fun findUserInformationByOauth2Session(sessionId: String): UserInformation?
}