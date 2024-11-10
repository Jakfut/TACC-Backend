package at.szybbs.tacc.taccbackend.runnable.acRunnable

import at.szybbs.tacc.taccbackend.factory.TeslaConnectionFactory
import java.util.*


// see flow diagram v0.2
class AcRunnable(
    private val teslaConnectionFactory: TeslaConnectionFactory,
    private val userId: UUID,
    private val targetState: Boolean
): Runnable {
    override fun run() {
        val teslaConnectionClient = teslaConnectionFactory.createTeslaConnectionClient(userId)

        teslaConnectionClient.changeAcState(targetState)
    }
}