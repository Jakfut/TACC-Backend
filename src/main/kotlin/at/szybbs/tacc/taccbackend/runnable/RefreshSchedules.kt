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

    @Scheduled(cron = "0 0 1 * * *")
    fun refreshSchedules() {
        logger.info("Refreshing schedules")

        val allUsers = userInformationService.getUserInformation()

        allUsers.forEach {
            val user = it
            val calendarClient = calendarConnectionFactory.createCalendarConnectionClient(user.id)

            val allEvents = calendarClient.getAllEventsWithKeyword(Instant.now())

            allEvents.forEach {
                if (it.location != null) {
                    schedulerService.scheduleLocation(
                        user.id,
                        true,
                        it.start,
                        it.location,
                        it.start.minusSeconds(60 * 60 * 2) // 2 hours before the event
                    )
                }
            }
        }

        logger.info("Schedules refreshed")
    }
}