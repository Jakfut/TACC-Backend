package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.client.teslaConnection.TessieConnectionClient
import at.szybbs.tacc.taccbackend.runnable.acRunnable.AcRunnable
import at.szybbs.tacc.taccbackend.runnable.acRunnable.scheduleAc
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.Instant
import java.util.*

@SpringBootApplication
class TaccBackendApplication

fun main(args: Array<String>) {
	//runApplication<TaccBackendApplication>(*args)

	/*scheduleAc(
		AcRunnable("123", UUID.randomUUID(), true),
		Instant.now().plusSeconds(60)
	)*/

	val tessieConnectionClient = TessieConnectionClient("XP7YGCEK0RB287024", "?")

	println(tessieConnectionClient.getLocation())
}
