package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.client.teslaConnection.TessieConnectionClient
import at.szybbs.tacc.taccbackend.factory.TeslaConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import java.util.*
import kotlin.test.Test

@SpringBootTest
class TeslaClientTests {
    @Autowired
    private lateinit var teslaConnectionFactory: TeslaConnectionFactory

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val teslaConnectionClient by lazy { teslaConnectionFactory.createTeslaConnectionClient(
        UUID.fromString("c8e2ceb5-f691-4952-a8d6-2a7e796cfbb8")) }

    @Test
    fun wake() {
        val result = teslaConnectionClient.wake()

        println("Wake: $result")

        assert(result)
    }

    @Test
    fun getStatus() {
        val status = teslaConnectionClient.getStatus()

        println("Status: $status")

        assert(status == "asleep" || status == "waiting_for_sleep" || status == "awake")
    }

    @Test
    fun getAcStatus() {
        val acStatus = teslaConnectionClient.getAcStatus()

        println("AC State: $acStatus")

        assert(true)
    }

    @Test
    fun getLocation() {
        val location = teslaConnectionClient.getLocation()

        println("Location: $location")
    }

    @Test
    fun changeAcStateToTrue() {
        val result = teslaConnectionClient.changeAcState(true)

        println("AC State: $result")

        assert(result)
    }

    @Test
    fun changeAcStateToFalse() {
        val result = teslaConnectionClient.changeAcState(false)

        println("AC State: $result")

        assert(result)
    }

    @Test
    fun testConnection() {
        val result = applicationContext.getBean(TessieConnectionClient::class.java).testConnection("", "")

        println("Connection: $result")

        assert(result)
    }
}