package at.szybbs.tacc.taccbackend.runnable.locationRunnable

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaLocation
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

// see flow diagram v0.2
class LocationRunnable(
    private val eventTime: LocalDateTime,
    private val vin: String,
    private val curLocation: TeslaLocation,
    private val tarLocation: TeslaLocation
) : Runnable {
    override fun run() {
        // TODO implement
        println("LocationRunnable: $eventTime, $vin, $curLocation, $tarLocation")
    }
}

fun scheduleLocation(task: LocationRunnable, instant: Instant) {
    SimpleAsyncTaskScheduler().schedule(task, instant)
}

fun scheduleLocation(task: LocationRunnable, hour: Int, minute: Int) {
    val instant = LocalDateTime.now()
        .withHour(hour)
        .withMinute(minute)
        .atZone(ZoneId.systemDefault())
        .toInstant()

    SimpleAsyncTaskScheduler().schedule(task, instant)
}