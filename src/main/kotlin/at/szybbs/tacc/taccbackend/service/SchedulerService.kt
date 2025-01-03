package at.szybbs.tacc.taccbackend.service

import at.szybbs.tacc.taccbackend.factory.TeslaConnectionFactory
import at.szybbs.tacc.taccbackend.runnable.acRunnable.AcRunnable
import at.szybbs.tacc.taccbackend.runnable.locationRunnable.LocationRunnable
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class SchedulerService(
    private val applicationContext: ApplicationContext
) {
    fun scheduleAc(userId: UUID, targetState: Boolean, instant: Instant) {
        val task = AcRunnable(applicationContext.getBean(TeslaConnectionFactory::class.java), userId, targetState)
        SimpleAsyncTaskScheduler().schedule(task, instant)
    }

    fun scheduleLocation(userId: UUID, targetState: Boolean, eventTime: Instant, tarLocation: String, instant: Instant) {
        val task = LocationRunnable(userId, targetState, eventTime, tarLocation)
        SimpleAsyncTaskScheduler().schedule(task, instant)
    }
}