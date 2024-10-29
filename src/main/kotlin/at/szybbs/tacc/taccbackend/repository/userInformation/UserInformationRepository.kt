package at.szybbs.tacc.taccbackend.repository.userInformation

import at.szybbs.tacc.taccbackend.model.userInformation.UserInformation
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Repository for CRUD operations on [UserInformation] entities in the database.
 */
interface UserInformationRepository : JpaRepository<UserInformation, UUID> {

}