package at.szybbs.tacc.taccbackend.job

import at.szybbs.tacc.taccbackend.factory.CalendarConnectionFactory
import at.szybbs.tacc.taccbackend.service.SchedulerService
import at.szybbs.tacc.taccbackend.service.UserInformationService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class RefreshSchedules(
    private val calendarConnectionFactory: CalendarConnectionFactory,
    private val schedulerService: SchedulerService,
    private val userInformationService: UserInformationService
) {
    private val logger = LoggerFactory.getLogger(RefreshSchedules::class.java)

    @Scheduled(cron = "0 0 0 * * *")
    fun refreshSchedules() {
        logger.info("Refreshing schedules")

        val allUsers = userInformationService.getUserInformation()

        allUsers.forEach { user ->
            logger.info("Refreshing schedules for user ${user.id}")
            val calendarClient = calendarConnectionFactory.createCalendarConnectionClient(user.id)

            val allEventsStart = calendarClient.getAllEventsWithKeywordStart(Instant.now())
            val allEventsEnd = calendarClient.getAllEventsWithKeywordEnd(Instant.now())

            allEventsStart.forEach {
                if (it.location != null) { // Event has a location
                    schedulerService.scheduleLocation(
                        user.id,
                        true,
                        it.start,
                        it.location,
                        Instant.now()
                    )
                } else { // Event has no location
                    schedulerService.scheduleAcWithRuntime(
                        user.id,
                        it.start.minusSeconds(user.noDestMinutes * 60L) // activate AC noDestMinutes before the event starts
                    )
                }
            }

            allEventsEnd.forEach { // end of event
                schedulerService.scheduleAcWithRuntime(
                    user.id,
                    it.end.minusSeconds(60 * 5) // activate AC 5 minutes before the end of the event ends
                )
            }
        }

        logger.info("Schedules refreshed")
    }
}