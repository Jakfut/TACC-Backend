package at.szybbs.tacc.taccbackend.runnable.locationRunnable

import at.szybbs.tacc.taccbackend.client.TaccDirections
import at.szybbs.tacc.taccbackend.service.SchedulerService
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class LocationJob : Job {
    @Autowired
    private lateinit var taccDirections: TaccDirections

    @Value("\${scheduling.time.unit}")
    private lateinit var timeUnit : String

    @Autowired
    private lateinit var schedulerService: SchedulerService

    private val logger = LoggerFactory.getLogger(javaClass) // Use a logger

    override fun execute(context: JobExecutionContext) {
        val userId = context.jobDetail.jobDataMap.getString("userId")?.let{ UUID.fromString(it)} ?: return
        val targetState = context.jobDetail.jobDataMap.getBoolean("targetState")
        val eventTime = Instant.ofEpochMilli(context.jobDetail.jobDataMap.getLong("eventTime"))
        val tarLocation = context.jobDetail.jobDataMap.getString("tarLocation") ?: return

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