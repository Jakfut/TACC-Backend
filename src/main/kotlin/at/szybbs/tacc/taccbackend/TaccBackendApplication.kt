package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.factory.TeslaConnectionFactory
import at.szybbs.tacc.taccbackend.runnable.acRunnable.AcRunnable
import at.szybbs.tacc.taccbackend.runnable.acRunnable.scheduleAc
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.Instant
import java.util.*

@SpringBootApplication
class TaccBackendApplication

fun main(args: Array<String>) {
	val applicationContext = runApplication<TaccBackendApplication>(*args)

	val teslaConnectionFactory: TeslaConnectionFactory = applicationContext.getBean(TeslaConnectionFactory::class.java)

	/*scheduleAc(
		AcRunnable(UUID.fromString("409cd7c7-e82d-406c-a784-621598ff45e9"), true, teslaConnectionFactory),
		Instant.now().plusSeconds(60)
	)*/
}
