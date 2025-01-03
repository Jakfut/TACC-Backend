package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.service.SchedulerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*
import kotlin.test.Test

@SpringBootTest
class LocationRunnableTests {
    @Autowired
    private lateinit var schedulerService: SchedulerService

    @Test
    fun scheduleAcOn() {
        schedulerService.scheduleLocation(
            UUID.fromString("409cd7c7-e82d-406c-a784-621598ff45e9"),
            true,
            Instant.now().plusSeconds(4),
            "tarLocation",
            Instant.now().plusSeconds(4)
        )

        Thread.sleep(20000) // spring would end the test before the scheduled task is executed
    }
}