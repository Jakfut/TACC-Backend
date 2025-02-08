package at.szybbs.tacc.taccbackend.service

import at.szybbs.tacc.taccbackend.entity.ScheduleEntry
import at.szybbs.tacc.taccbackend.job.AcJob
import at.szybbs.tacc.taccbackend.job.LocationJob
import org.quartz.JobBuilder
import org.quartz.JobKey
import org.quartz.Scheduler
import org.quartz.TriggerBuilder
import org.quartz.impl.matchers.GroupMatcher
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class SchedulerService(
    private val scheduler: Scheduler,
) {
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

    fun scheduleLocation(userId: UUID, targetState: Boolean, eventTime: Instant, tarLocation: String, instant: Instant) {
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
                jobDetail.key.group,
                jobDetail.key.group == "acGroup",
                jobDetail.key.group == "locationGroup",
            )
        }
    }

    fun unscheduleJob(jobKey: JobKey) {
        scheduler.deleteJob(jobKey)
    }
}