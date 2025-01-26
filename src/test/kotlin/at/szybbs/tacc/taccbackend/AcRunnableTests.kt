package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.service.SchedulerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*
import kotlin.test.Test

@SpringBootTest
class AcRunnableTests {
    @Autowired
    private lateinit var schedulerService: SchedulerService

    @Test
    fun scheduleAcOn() {
        schedulerService.scheduleAc(
            UUID.fromString("c8e2ceb5-f691-4952-a8d6-2a7e796cfbb8"),
            true,
            Instant.now().plusSeconds(4)
        )

        Thread.sleep(20000) // spring would end the test before the scheduled task is executed
    }
}