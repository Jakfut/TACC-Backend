package at.szybbs.tacc.taccbackend.runnable

import at.szybbs.tacc.taccbackend.client.TaccDirections
import at.szybbs.tacc.taccbackend.client.teslaConnection.TeslaConnectionClient
import at.szybbs.tacc.taccbackend.factory.CalendarConnectionFactory
import at.szybbs.tacc.taccbackend.factory.TeslaConnectionFactory
import at.szybbs.tacc.taccbackend.service.SchedulerService
import at.szybbs.tacc.taccbackend.service.userInformation.UserInformationService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class RefreshSchedules(
    private val taccDirections: TaccDirections,
    private val teslaConnectionFactory: TeslaConnectionFactory,
    private val calendarConnectionFactory: CalendarConnectionFactory,
    private val schedulerService: SchedulerService,
    private val userInformationService: UserInformationService
) {
    private val logger = LoggerFactory.getLogger(RefreshSchedules::class.java)

    @Scheduled(cron = "0 30 1 * * *")
    fun refreshSchedules() {
        logger.info("Refreshing schedules")

        val allUsers = userInformationService.getUserInformation()

        allUsers.forEach {
            val user = it
            logger.info("Refreshing schedules for user ${user.id}")
            val calendarClient = calendarConnectionFactory.createCalendarConnectionClient(user.id)

            val allEvents = calendarClient.getAllEventsWithKeyword(Instant.now())

            allEvents.forEach {
                if (it.location != null) {
                    schedulerService.scheduleLocation(
                        user.id,
                        true,
                        it.start,
                        it.location,
                        it.start.minusSeconds(60 * 60 * 2) // check again 2 hours before the start of the event
                    )
                    schedulerService.scheduleAc(
                        user.id,
                        true,
                        it.end.minusSeconds(60 * 5) // activate AC 5 minutes before the end of the event ends
                    )
                }
            }
        }

        logger.info("Schedules refreshed")
    }
}