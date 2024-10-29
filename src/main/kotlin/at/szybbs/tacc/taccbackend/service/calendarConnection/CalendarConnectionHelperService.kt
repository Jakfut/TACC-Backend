package at.szybbs.tacc.taccbackend.service.calendarConnection

import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationNotFoundException
import at.szybbs.tacc.taccbackend.repository.calendarConnection.CalendarConnectionRepository
import at.szybbs.tacc.taccbackend.service.userInformation.UserInformationService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class CalendarConnectionHelperService (
    private val calendarConnectionRepository: CalendarConnectionRepository,
    private val userInformationService: UserInformationService
) {
    /**
     * Sets all active CalendarConnections for a specified user to inactive.
     * This retrieves and deactivates any CalendarConnections associated with the given `userInformationId`.
     *
     * @param userInformationId UUID of the user whose active connections will be deactivated
     * @throws UserInformationNotFoundException if no information is found for the specified user
     */
    @Transactional
    fun setAllCalendarConnectionsToInactive(userInformationId: UUID) {
        if (!userInformationService.userInformationExists(userInformationId)) throw UserInformationNotFoundException(userInformationId)

        val activeCalendarConnections = calendarConnectionRepository.findByUserInformationIdAndActiveIsTrue(userInformationId)

        for (calendarConnection in activeCalendarConnections) {
            calendarConnection.active = false
        }
    }
}