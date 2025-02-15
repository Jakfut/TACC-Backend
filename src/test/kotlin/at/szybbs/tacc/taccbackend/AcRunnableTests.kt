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
            UUID.fromString("e50d926b-f45d-4c0a-9345-0536c04b8162"),
            true,
            Instant.now(),
            "",
            Instant.now().plusSeconds(4)
        )

        Thread.sleep(20000) // spring would end the test before the scheduled task is executed
    }
}