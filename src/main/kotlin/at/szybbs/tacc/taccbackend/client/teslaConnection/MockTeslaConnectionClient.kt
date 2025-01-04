package at.szybbs.tacc.taccbackend.client.teslaConnection

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
@Scope("prototype")
class MockTeslaConnectionClient : TeslaConnectionClient {
    override lateinit var userId: UUID

    override fun getType(): TeslaConnectionType {
        return TeslaConnectionType.MOCK
    }

    override fun wake(): Boolean {
        return true
    }

    override fun getLocation(): String {
        // get current hour
        val hour = Instant.now().epochSecond / 3600 % 24

        when (hour) {
            in 0..6 -> return "Feichsen 27"
            in 7..12 -> return "Wien"
            in 13..18 -> return "Feichsen 27"
            in 19..23 -> return "Feichsen 27"
        }
        return "Feichsen 27"
    }

    override fun getStatus(): String {
        return "awake"
    }

    override fun changeAcState(state: Boolean): Boolean {
        return true
    }
}