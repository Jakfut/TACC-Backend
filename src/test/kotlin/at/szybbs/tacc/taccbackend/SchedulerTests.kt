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

    @Test
    fun scheduleAcOn() {
        schedulerService.scheduleLocation(
            UUID.fromString("c8e2ceb5-f691-4952-a8d6-2a7e796cfbb8"),
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
            UUID.fromString("c8e2ceb5-f691-4952-a8d6-2a7e796cfbb8"),
            true,
            Instant.now().plusSeconds(60L * 60L), // the event is in one hour
            "Ybbs an der Donau, Österreich",
            Instant.now().plusSeconds(4)
        )

        schedulerService.scheduleLocation(
            UUID.fromString("4b8712d8-47dd-40bf-b466-3e7e2c64b785"),
            true,
            Instant.now().plusSeconds(60L * 60L), // the event is in one hour
            "Ybbs an der Donau, Österreich",
            Instant.now().plusSeconds(4)
        )

        Thread.sleep(1000 * 6)

        scheduler
            .getTriggersOfJob(schedulerService
                .getScheduledJobsForUser(UUID
                    .fromString("c8e2ceb5-f691-4952-a8d6-2a7e796cfbb8")).first()).forEach {
                println(it.nextFireTime)
            }
    }

    @Test
    fun getScheduleEntries() {
        schedulerService.scheduleLocation(
            UUID.fromString("c8e2ceb5-f691-4952-a8d6-2a7e796cfbb8"),
            true,
            Instant.now().plusSeconds(60L * 60L), // the event is in one hour
            "Ybbs an der Donau, Österreich",
            Instant.now().plusSeconds(4)
        )

        schedulerService.scheduleLocation(
            UUID.fromString("4b8712d8-47dd-40bf-b466-3e7e2c64b785"),
            true,
            Instant.now().plusSeconds(60L * 60L), // the event is in one hour
            "Ybbs an der Donau, Österreich",
            Instant.now().plusSeconds(4)
        )

        Thread.sleep(1000 * 6)

        val result = schedulerService.getScheduleEntries(UUID.fromString("c8e2ceb5-f691-4952-a8d6-2a7e796cfbb8"))

        result.forEach {
            println(it)
        }
    }
}