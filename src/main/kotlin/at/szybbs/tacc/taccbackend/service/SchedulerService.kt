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
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.absoluteValue

@Service
class SchedulerService(
    private val scheduler: Scheduler,
    private val userInformationService: UserInformationService,
) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun scheduleAc(userId: UUID, targetState: Boolean, instant: Instant) {
        val jobDetail = JobBuilder.newJob(AcJob::class.java)
            .withIdentity("acJob-$userId", "acGroup")
            .usingJobData("userId", userId.toString()) // Pass data to the job
            .usingJobData("targetState", targetState)
            .storeDurably(true) // Allow replacing the job if it already exists
            .build()
        val trigger = TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity("acTrigger-$userId", "acGroup")
            .startAt(Date.from(instant)) // Schedule for the given instant
            .build()

        if (scheduler.checkExists(jobDetail.key)) {
            scheduler.deleteJob(jobDetail.key) // unschedule if exists
        }
        scheduler.scheduleJob(jobDetail, trigger)
    }

    fun scheduleAcWithRuntime(userId: UUID, instant: Instant) {
        if (checkForSimilarJob(userId, "acGroup", instant)) {
            logger.info("Skipping scheduling of AC job for user $userId at $instant because there is already a similar job")
            return // Skip if there is already a similar job
        }

        val user = userInformationService.getUserInformation(userId)

        if (user.ccRuntimeMinutes == 0) { // User has no runtime set
            scheduleAc(userId, true, instant)
            return
        }

        for (i in 0 until user.ccRuntimeMinutes / 5){
            scheduleAc(userId, true, instant.plusSeconds(i.toLong() * 5 * 60))
        }

        scheduleAc(userId, false, instant.plusSeconds(user.ccRuntimeMinutes.toLong() * 60))
    }

    fun scheduleLocation(userId: UUID, targetState: Boolean, eventTime: Instant, tarLocation: String, instant: Instant) {
        if (checkForSimilarJob(userId, "locationGroup", eventTime, tarLocation)) {
            logger.info("Skipping scheduling of Location job for user $userId at $eventTime because there is already a similar job")
            return // Skip if there is already a similar job
        }

        val jobDetail = JobBuilder.newJob(LocationJob::class.java)
            .withIdentity("locationJob-$userId-${eventTime.toEpochMilli()}", "locationGroup") // Unique ID!
            .usingJobData("userId", userId.toString())
            .usingJobData("targetState", targetState)
            .usingJobData("eventTime", eventTime.toEpochMilli())
            .usingJobData("tarLocation", tarLocation)
            .storeDurably(true) // Very important for rescheduling
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

        return jobs.map {
            val jobDetail = scheduler.getJobDetail(it)
            val trigger = scheduler.getTriggersOfJob(it).first()

            ScheduleEntry(
                trigger.nextFireTime.toInstant(),
                (jobDetail.jobDataMap.get("eventTime") as? Long)?.let { Instant.ofEpochMilli(it) },
                jobDetail.jobDataMap.getString("tarLocation") ?: "",
                jobDetail.key.group == "acGroup",
                jobDetail.key.group == "locationGroup",
            )
        }
    }

    fun checkForSimilarJob(userId: UUID, group: String, eventTime: Instant? = null, tarLocation: String? = null): Boolean {
        val jobKeys = getScheduledJobsForUser(userId)
        val fiveMinutes = 5L * 60L // 5 minutes in seconds

        for (jobKey in jobKeys) {
            if (jobKey.group != group) continue  // Skip jobs from other groups

            val jobDetail = scheduler.getJobDetail(jobKey)
            val triggers = scheduler.getTriggersOfJob(jobKey)

            for (trigger in triggers) { // usually only one trigger
                if (trigger.nextFireTime == null) continue
                val nextFireTime = trigger.nextFireTime.toInstant()

                when (group) {
                    "acGroup" -> {
                        // Check if the job is scheduled within 5 minutes of the given instant
                        if (ChronoUnit.SECONDS.between(eventTime, nextFireTime).absoluteValue <= fiveMinutes) {
                            return true // Found a similar AC job
                        }
                    }
                    "locationGroup" -> {
                        // Check for same location and start time
                        if(eventTime != null && tarLocation != null){
                            val jobEventTime = (jobDetail.jobDataMap.get("eventTime") as? Long)?.let { Instant.ofEpochMilli(it) }
                            val jobTarLocation = jobDetail.jobDataMap.getString("tarLocation")

                            if (ChronoUnit.SECONDS.between(jobEventTime, eventTime).absoluteValue <= fiveMinutes && jobTarLocation == tarLocation) {
                                return true // Found a similar Location job
                            }
                        }
                    }
                }
            }
        }

        return false // No similar job found
    }

    fun unscheduleJob(jobKey: JobKey) {
        scheduler.deleteJob(jobKey)
    }
}