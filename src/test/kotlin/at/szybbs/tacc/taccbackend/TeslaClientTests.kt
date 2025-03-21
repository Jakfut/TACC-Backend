package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.client.teslaConnection.TessieConnectionClient
import at.szybbs.tacc.taccbackend.factory.TeslaConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.Test

@SpringBootTest
class TeslaClientTests {
    @Autowired
    private lateinit var teslaConnectionFactory: TeslaConnectionFactory

    private val teslaConnectionClient by lazy { teslaConnectionFactory.createTeslaConnectionClient(
        UUID.fromString("e50d926b-f45d-4c0a-9345-0536c04b8162")) }

    @Autowired
    private lateinit var tessieConnectionClient: TessieConnectionClient

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
        val result = tessieConnectionClient.testConnection("?", "?")

        println("Connection: $result")

        assert(result)
    }

    @Test
    fun testIsUserPresent() {
        val result = teslaConnectionClient.isUserPresent()

        println("User Present: $result")

        assert(result)
    }
}