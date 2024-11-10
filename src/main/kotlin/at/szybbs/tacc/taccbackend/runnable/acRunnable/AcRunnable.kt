package at.szybbs.tacc.taccbackend.runnable.acRunnable

import at.szybbs.tacc.taccbackend.factory.TeslaConnectionFactory
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


// see flow diagram v0.2
class AcRunnable(
    private val userId: UUID,
    private val targetState: Boolean,
    private val teslaConnectionFactory: TeslaConnectionFactory
) : Runnable {
    override fun run() {
        val teslaConnectionClient = teslaConnectionFactory.createTeslaConnectionClient(userId)

        teslaConnectionClient.changeAcState(targetState)
    }
}

fun scheduleAc(task: AcRunnable, instant: Instant) {
    SimpleAsyncTaskScheduler().schedule(task, instant)
}

fun scheduleAc(task: AcRunnable, hour: Int, minute: Int) {
    val instant = LocalDateTime.now()
        .withHour(hour)
        .withMinute(minute)
        .atZone(ZoneId.systemDefault())
        .toInstant()

    SimpleAsyncTaskScheduler().schedule(task, instant)
}