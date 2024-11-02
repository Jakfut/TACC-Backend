package at.szybbs.tacc.taccbackend.runnable.acRunnable

import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


// see flow diagram v0.2
class AcRunnable(
    private val vin: String,
    private val userId: UUID,
    private val targetState: Boolean
) : Runnable {
    override fun run() {
        // TODO implement
        println("ACRunnable: $vin, $userId, $targetState")
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