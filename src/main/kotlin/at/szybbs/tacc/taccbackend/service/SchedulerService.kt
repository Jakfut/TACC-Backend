package at.szybbs.tacc.taccbackend.service

import at.szybbs.tacc.taccbackend.entity.ScheduleEntry
import at.szybbs.tacc.taccbackend.job.AcJob
import at.szybbs.tacc.taccbackend.job.LocationJob
import org.quartz.JobBuilder
import org.quartz.JobKey
import org.quartz.Scheduler
import org.quartz.TriggerBuilder
import org.quartz.impl.matchers.GroupMatcher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class SchedulerService(
    private val scheduler: Scheduler,
    private val userInformationService: UserInformationService,
) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun scheduleAc(userId: UUID, targetState: Boolean, eventTime: Instant, tarLocation: String, instant: Instant, isForScheduleEntry: Boolean = false) {
        val jobDetail = JobBuilder.newJob(AcJob::class.java)
            .withIdentity("acJob-$userId-${instant.toEpochMilli()}", "acGroup")
            .usingJobData("userId", userId.toString()) // Pass data to the job
            .usingJobData("targetState", targetState)
            .usingJobData("eventTime", eventTime.toEpochMilli())
            .usingJobData("tarLocation", tarLocation)
            .usingJobData("isForScheduleEntry", isForScheduleEntry)
            .build()
        val trigger = TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity("acTrigger-$userId-${instant.toEpochMilli()}", "acGroup")
            .startAt(Date.from(instant)) // Schedule for the given instant
            .build()

        if (scheduler.checkExists(jobDetail.key)) {
            scheduler.deleteJob(jobDetail.key) // unschedule if exists
        }
        scheduler.scheduleJob(jobDetail, trigger)

        logger.info("Scheduled AC job for user $userId at $instant with targetState $targetState")
    }

    fun scheduleAcWithRuntime(userId: UUID, eventTime: Instant, tarLocation: String, instant: Instant) {
        val user = userInformationService.getUserInformation(userId)

        if (user.ccRuntimeMinutes == 0) { // User has no runtime set
            scheduleAc(userId, true, eventTime, tarLocation, instant)
            return
        }

        for (i in 0 until user.ccRuntimeMinutes / 5){
            if (i == 0) {
                // The first job should be relevant to the ScheduleEntries
                scheduleAc(userId, true, eventTime, tarLocation, instant.plusSeconds(i.toLong() * 5 * 60), true)
            }

            scheduleAc(userId, true, eventTime, tarLocation, instant.plusSeconds(i.toLong() * 5 * 60))
        }
    }

    fun scheduleLocation(userId: UUID, targetState: Boolean, eventTime: Instant, tarLocation: String, instant: Instant) {
        val jobDetail = JobBuilder.newJob(LocationJob::class.java)
            .withIdentity("locationJob-$userId-${eventTime.toEpochMilli()}", "locationGroup") // Unique ID!
            .usingJobData("userId", userId.toString())
            .usingJobData("targetState", targetState)
            .usingJobData("eventTime", eventTime.toEpochMilli())
            .usingJobData("tarLocation", tarLocation)
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity("locationTrigger-$userId-${eventTime.toEpochMilli()}", "locationGroup")
            .startAt(Date.from(instant))
            .build()

        if (scheduler.checkExists(jobDetail.key)) {
            scheduler.deleteJob(jobDetail.key)
        }
        scheduler.scheduleJob(jobDetail, trigger)
    }

    fun getScheduledJobsForUser(userId: UUID): List<JobKey> {
        val acGroupMatcher = GroupMatcher.jobGroupEquals("acGroup")
        val locationGroupMatcher = GroupMatcher.jobGroupEquals("locationGroup")

        val acJobs = scheduler.getJobKeys(acGroupMatcher).filter { it.name.contains(userId.toString()) }
        val locationJobs = scheduler.getJobKeys(locationGroupMatcher).filter { it.name.contains(userId.toString()) }

        return acJobs + locationJobs
    }

    fun getScheduleEntries(userId: UUID): List<ScheduleEntry> {
        val jobs = getScheduledJobsForUser(userId)

        return jobs.mapNotNull {
            val jobDetail = scheduler.getJobDetail(it)
            val trigger = scheduler.getTriggersOfJob(it).firstOrNull()

            if (trigger == null) {
                logger.warn("No trigger found for job ${it.name}")
                return@mapNotNull null
            }

            if (jobDetail.key.group == "acGroup") { // Check if it's an AC Job
                val isForScheduleEntry = jobDetail.jobDataMap.getBoolean("isForScheduleEntry") // Default to false if not present
                if (!isForScheduleEntry) { // Skip if isForScheduleEntry is false
                    return@mapNotNull null
                }
            }

            ScheduleEntry(
                trigger.nextFireTime.toInstant(),
                (jobDetail.jobDataMap.get("eventTime") as? Long)?.let { Instant.ofEpochMilli(it) } ?: Instant.now(),
                jobDetail.jobDataMap.getString("tarLocation") ?: "",
                jobDetail.key.group == "acGroup",
                jobDetail.key.group == "locationGroup",
            )
        }
    }

    fun unscheduleJob(jobKey: JobKey) {
        scheduler.deleteJob(jobKey)
    }
}