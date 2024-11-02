package at.szybbs.tacc.taccbackend.factory

import at.szybbs.tacc.taccbackend.client.teslaConnection.TeslaConnectionClient
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnectionType
import jakarta.annotation.PostConstruct
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service

@Service
class TeslaConnectionFactory(
    private val applicationContext: ApplicationContext,
    private val connectionClients: List<TeslaConnectionClient>
) {

    private val clientCache = mutableMapOf<TeslaConnectionType, TeslaConnectionClient>()

    @PostConstruct
    fun initClientCache() {
        for (client in connectionClients) {
            clientCache[client.getType()] = client
        }
    }

    fun createTeslaConnectionClient(type: TeslaConnectionType, vin: String, token: String): TeslaConnectionClient {
        val clientPrototype = clientCache[type] ?: throw RuntimeException("Unknown client type: $type")
        return applicationContext.getBean(clientPrototype::class.java).apply { this.vin = vin; this.token = token }
    }
}