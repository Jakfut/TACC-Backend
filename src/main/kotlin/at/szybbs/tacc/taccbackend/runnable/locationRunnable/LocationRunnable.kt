package at.szybbs.tacc.taccbackend.runnable.locationRunnable

import java.time.Instant
import java.util.*

// see flow diagram v0.2
class LocationRunnable(
    private val userId: UUID,
    private val targetState: Boolean,
    private val eventTime: Instant,
    private val tarLocation: String
) : Runnable {
    override fun run() {
        // TODO implement
        println("LocationRunnable: $eventTime, $tarLocation")
    }
}