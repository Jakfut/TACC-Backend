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
    private val applicationContext: ApplicationContext,
    private val timeUnit: String
) : Runnable {
    override fun run(
    ) {
        val schedulerService = applicationContext.getBean(SchedulerService::class.java)
        val taccDirections = applicationContext.getBean(TaccDirections::class.java)
        val logger = LoggerFactory.getLogger(javaClass)

        val driveTime = taccDirections.getDriveTimeFromCurrentLocationWithVariables(tarLocation, userId)

        val multiplier = if (timeUnit == "seconds") 1 else 60

        val timeDifference = (eventTime.toEpochMilli() - Instant.now().toEpochMilli() - driveTime * 1000 * multiplier) / (1000 * multiplier)

        println(timeDifference)

        when (timeDifference) {
            in Int.MIN_VALUE..0 -> {
                logger.error("TimeDifference is negative")
            }
            in 0..15 -> {
                logger.info("Inserted into AcRunnable with timeDifference: $timeDifference")
                schedulerService.scheduleAc(userId, targetState, Instant.now().plusSeconds(timeDifference * multiplier))
            }
            in 16..60 -> {
                logger.info("Inserted into LocationRunnable, check again in 15 $timeUnit")
                schedulerService.scheduleLocation(userId, targetState, eventTime, tarLocation, Instant.now().plusSeconds(15L * multiplier))
            }
            else -> {
                logger.info("Inserted into LocationRunnable, check again in 60 $timeUnit")
                schedulerService.scheduleLocation(userId, targetState, eventTime, tarLocation, Instant.now().plusSeconds(60L * multiplier))
            }
        }
    }
}