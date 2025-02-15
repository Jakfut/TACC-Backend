package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.service.SchedulerService
import org.quartz.Scheduler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*
import kotlin.test.Test

@SpringBootTest
class SchedulerTests {
    @Autowired
    private lateinit var schedulerService: SchedulerService

    @Autowired
    private lateinit var scheduler: Scheduler

    @Value("\${scheduling.time.unit}")
    private val timeUnit = ""

    private val multiplier by lazy { if (timeUnit == "seconds") 1 else 60 }

    private val userId = "e50d926b-f45d-4c0a-9345-0536c04b8162"

    @Test
    fun scheduleAcOn() {
        schedulerService.scheduleLocation(
            UUID.fromString(userId),
            true,
            Instant.now().plusSeconds(60L * multiplier), // the event is in one hour
            "Ybbs an der Donau, Österreich",
            Instant.now().plusSeconds(4)
        )

        Thread.sleep(1000 * 60 * 60) // spring would end the test before the scheduled task is executed
    }

    @Test
    fun getTasksOfUser() {
        schedulerService.scheduleLocation(
            UUID.fromString(userId),
            true,
            Instant.now().plusSeconds(60L * 60L), // the event is in one hour
            "Ybbs an der Donau, Österreich",
            Instant.now().plusSeconds(4)
        )

        schedulerService.scheduleLocation(
            UUID.fromString(userId),
            true,
            Instant.now().plusSeconds(60L * 60L), // the event is in one hour
            "Ybbs an der Donau, Österreich",
            Instant.now().plusSeconds(4)
        )

        Thread.sleep(1000 * 6)

        scheduler
            .getTriggersOfJob(schedulerService
                .getScheduledJobsForUser(UUID
                    .fromString(userId)).first()).forEach {
                println(it.nextFireTime)
            }
    }

    @Test
    fun getScheduleEntries() {
        schedulerService.scheduleLocation(
            UUID.fromString(userId),
            true,
            Instant.now().plusSeconds(60L * 60L), // the event is in one hour
            "Ybbs an der Donau, Österreich",
            Instant.now().plusSeconds(4)
        )

        schedulerService.scheduleAc(
            UUID.fromString(userId),
            true,
            Instant.now(),
            "",
            Instant.now().plusSeconds(60L * 60L), // the event is in one hour
        )

        schedulerService.scheduleLocation(
            UUID.fromString("24867593-837b-4eda-86c9-199e1b03d229"),
            true,
            Instant.now().plusSeconds(60L * 60L), // the event is in one hour
            "Ybbs an der Donau, Österreich",
            Instant.now().plusSeconds(4)
        )

        Thread.sleep(1000 * 6)

        val result = schedulerService.getScheduleEntries(UUID.fromString(userId))

        result.forEach {
            println(it)
        }

        assert(result.isNotEmpty())
    }
}