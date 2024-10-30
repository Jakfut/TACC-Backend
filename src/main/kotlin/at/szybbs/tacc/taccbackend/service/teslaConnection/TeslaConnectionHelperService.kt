package at.szybbs.tacc.taccbackend.service.teslaConnection

import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationNotFoundException
import at.szybbs.tacc.taccbackend.repository.teslaConnection.TeslaConnectionRepository
import at.szybbs.tacc.taccbackend.service.userInformation.UserInformationService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class TeslaConnectionHelperService (
    private val teslaConnectionRepository: TeslaConnectionRepository,
    private val userInformationService: UserInformationService
) {
    /**
     * Sets all active TeslaConnections for a specified user to inactive.
     * This retrieves and deactivates any TeslaConnections associated with the given `userInformationId`.
     *
     * @param userInformationId UUID of the user whose active connections will be deactivated
     * @throws UserInformationNotFoundException if no information is found for the specified user
     */
    @Transactional
    fun setAllTeslaConnectionsToInactive(userInformationId: UUID) {
        if (!userInformationService.userInformationExists(userInformationId)) throw UserInformationNotFoundException(userInformationId)

        val activeTeslaConnections = teslaConnectionRepository.findByUserInformationIdAndActiveIsTrue(userInformationId)

        for (teslaConnection in activeTeslaConnections) {
            teslaConnection.active = false
        }
    }
}