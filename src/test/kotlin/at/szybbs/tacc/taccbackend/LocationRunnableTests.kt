package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.service.SchedulerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*
import kotlin.test.Test

@SpringBootTest
class LocationRunnableTests {
    @Autowired
    private lateinit var schedulerService: SchedulerService

    @Value("\${scheduling.time.unit}")
    private val timeUnit = ""

    private val multiplier by lazy { if (timeUnit == "seconds") 1 else 60 }

    @Test
    fun scheduleAcOn() {
        schedulerService.scheduleLocation(
            UUID.fromString("409cd7c7-e82d-406c-a784-621598ff45e9"),
            true,
            Instant.now().plusSeconds(60L * multiplier), // the event is in one hour
            "Ybbs an der Donau, Österreich",
            Instant.now().plusSeconds(4)
        )

        Thread.sleep(1000 * 60 * 60) // spring would end the test before the scheduled task is executed
    }
}