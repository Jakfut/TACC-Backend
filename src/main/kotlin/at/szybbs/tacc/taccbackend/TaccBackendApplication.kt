package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.client.teslaConnection.TessieConnectionClient
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
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

	/*scheduleAc(
		AcRunnable("123", UUID.randomUUID(), true),
		Instant.now().plusSeconds(60)
	)*/

	/*val tessieConnectionClient = TessieConnectionClient("XP7YGCEK0RB287024", "6lVasVXk1L2Xy6tOusLYvmCPSRpNklgM")

	println(tessieConnectionClient.getLocation())*/

	// get the connection factory bean
	val connectionFactory = applicationContext.getBean(TeslaConnectionFactory::class.java)

	// create a tesla connection client
	val teslaConnectionClient = connectionFactory.createTeslaConnectionClient(
		TeslaConnectionType.TESSIE,
		"XP7YGCEK0RB287024",
		"?"
	)

	// get the location of the car
	println(teslaConnectionClient.getLocation())
}
