package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.factory.TeslaConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.Test

@SpringBootTest
class TessieClientTests {
    @Autowired
    private lateinit var teslaConnectionFactory: TeslaConnectionFactory

    private val teslaConnectionClient by lazy { teslaConnectionFactory.createTeslaConnectionClient(
        UUID.fromString("409cd7c7-e82d-406c-a784-621598ff45e9")) }

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
}