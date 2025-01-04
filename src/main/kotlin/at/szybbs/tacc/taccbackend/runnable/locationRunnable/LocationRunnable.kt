package at.szybbs.tacc.taccbackend.runnable.locationRunnable

import at.szybbs.tacc.taccbackend.client.TaccDirections
import at.szybbs.tacc.taccbackend.service.SchedulerService
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import java.time.Instant
import java.util.*

// see flow diagram v0.2
class LocationRunnable(
    private val userId: UUID,
    private val targetState: Boolean,
    private val eventTime: Instant,
    private val tarLocation: String,
    private val applicationContext: ApplicationContext
) : Runnable {
    override fun run(
    ) {
        val schedulerService = applicationContext.getBean(SchedulerService::class.java)
        val taccDirections = applicationContext.getBean(TaccDirections::class.java)
        val logger = LoggerFactory.getLogger(javaClass)

        val driveTime = taccDirections.getDriveTimeFromCurrentLocationWithVariables(tarLocation, userId)

        val timeDifference = (eventTime.toEpochMilli() - Instant.now().toEpochMilli() - driveTime * 60000) / 60000

        when (timeDifference) {
            in 0..15 -> {
                logger.info("Inserted into AcRunnable with timeDifference: $timeDifference")
                schedulerService.scheduleAc(userId, targetState, Instant.now().plusSeconds(timeDifference * 60))
            }
            in 16..60 -> {
                logger.info("Inserted into LocationRunnable, check again in 15 minutes")
                schedulerService.scheduleLocation(userId, targetState, eventTime, tarLocation, Instant.now().plusSeconds(15 * 60))
            }
            else -> {
                logger.info("Inserted into LocationRunnable, check again in 60 minutes")
                schedulerService.scheduleLocation(userId, targetState, eventTime, tarLocation, Instant.now().plusSeconds(60 * 60))
            }
        }
    }
}