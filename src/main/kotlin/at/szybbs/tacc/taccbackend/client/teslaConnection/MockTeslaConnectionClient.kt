package at.szybbs.tacc.taccbackend.client.teslaConnection

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Component
@Scope("prototype")
class MockTeslaConnectionClient : TeslaConnectionClient {
    override lateinit var userId: UUID

    private val logger = LoggerFactory.getLogger(MockTeslaConnectionClient::class.java)

    override fun getType(): TeslaConnectionType {
        return TeslaConnectionType.MOCK
    }

    override fun wake(): Boolean {
        logger.info("Waking up mock tesla")

        return true
    }

    override fun getLocation(): String {
        // get current hour
        val now = ZonedDateTime.now(ZoneId.systemDefault()) // Get current time in your system's timezone
        val hour = now.hour

        logger.info("Getting mock location for hour $hour")

        when (hour) {
            in 0..6 -> return "Feichsen 27"
            in 7..12 -> return "Wien"
            in 13..18 -> return "Feichsen 27"
            in 19..23 -> return "Feichsen 27"
        }
        return "Feichsen 27"
    }

    override fun getStatus(): String {
        logger.info("Getting mock status")

        return "awake"
    }

    override fun changeAcState(state: Boolean): Boolean {
        logger.info("Changing mock AC state to $state")

        return true
    }
}