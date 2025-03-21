package at.szybbs.tacc.taccbackend.job

import at.szybbs.tacc.taccbackend.client.TaccDirections
import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import at.szybbs.tacc.taccbackend.factory.CalendarConnectionFactory
import at.szybbs.tacc.taccbackend.service.SchedulerService
import at.szybbs.tacc.taccbackend.service.UserInformationService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class RefreshSchedules(
    private val calendarConnectionFactory: CalendarConnectionFactory,
    private val schedulerService: SchedulerService,
    private val userInformationService: UserInformationService,
    private val taccDirections: TaccDirections
) {
    private val logger = LoggerFactory.getLogger(RefreshSchedules::class.java)

    @Scheduled(cron = "0 0 1 * * 6")
    fun refreshSchedules() {
        logger.info("Refreshing schedules")

        val allUsers = userInformationService.getUserInformation()

        allUsers.forEach { user ->
            refreshUserSchedules(user)
        }

        logger.info("Schedules refreshed")
    }

    fun refreshUserSchedules(user: UserInformation) {
        logger.info("Refreshing schedules for user ${user.id}")

        try {
            val calendarClient = calendarConnectionFactory.createCalendarConnectionClient(user.id)
            val allEventsStart = calendarClient.getAllEventsWithKeywordStart(Instant.now())
            val allEventsEnd = calendarClient.getAllEventsWithKeywordEnd(Instant.now())

            allEventsStart.forEach {
                if (it.location != null) { // Event has a location
                    val driveTime = taccDirections.getDriveTimeFromCurrentLocationWithVariables(it.location, user.id)
                    val timeDifference = (it.start.toEpochMilli() - Instant.now().toEpochMilli() - driveTime * 1000 * 60) / (1000 * 60)

                    val scheduleTime = if (timeDifference < 60 * 12) {
                        Instant.now().plusSeconds(60)
                    } else {
                        Instant.now().plusSeconds((timeDifference * 60) - 60 * 12)
                    }

                    schedulerService.scheduleLocation(
                        user.id,
                        true,
                        it.start,
                        it.location,
                        scheduleTime
                    )
                    logger.info("Scheduled location for user ${user.id} at ${it.start}")
                } else { // Event has no location
                    schedulerService.scheduleAcWithRuntime(
                        user.id,
                        it.end,
                        "",
                        it.start.minusSeconds(user.noDestMinutes * 60L) // activate AC noDestMinutes before the event starts
                    )
                    logger.info("Scheduled AC for user ${user.id} at ${it.start}")
                }
            }

            allEventsEnd.forEach { // end of event
                // check if end of event is in the past
                if (it.end.isBefore(Instant.now())) {
                    return@forEach
                }

                schedulerService.scheduleAcWithRuntime(
                    user.id,
                    it.end,
                    it.location ?: "",
                    it.end.minusSeconds(60 * 5) // activate AC 5 minutes before the end of the event ends
                )
                logger.info("Scheduled AC for user ${user.id} at ${it.end}")
            }
        } catch (e: ClientAuthorizationRequiredException) {
            logger.warn("Could not refresh schedules for user ${user.id} due to missing authorization.")
        } catch (e: Exception) {
            logger.error("Error refreshing schedules for user ${user.id}: ${e.message}", e)
        }
    }
}