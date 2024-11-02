package at.szybbs.tacc.taccbackend.service.userInformation


import at.szybbs.tacc.taccbackend.repository.userInformation.UserInformationRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserInformationService (
    private val userInformationRepository: UserInformationRepository
) {

    fun userInformationExists(userInformationId: UUID): Boolean {
        return userInformationRepository.findById(userInformationId).isPresent
    }

}