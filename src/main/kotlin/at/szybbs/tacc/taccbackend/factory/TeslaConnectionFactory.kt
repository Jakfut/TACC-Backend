package at.szybbs.tacc.taccbackend.factory

import at.szybbs.tacc.taccbackend.client.teslaConnection.TeslaConnectionClient
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import at.szybbs.tacc.taccbackend.service.UserInformationService
import jakarta.annotation.PostConstruct
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TeslaConnectionFactory(
    private val applicationContext: ApplicationContext,
    private val connectionClients: List<TeslaConnectionClient>,
    private var userInformationService: UserInformationService
) {
    private val clientCache = mutableMapOf<TeslaConnectionType, TeslaConnectionClient>()

    @PostConstruct
    fun initClientCache() {
        for (client in connectionClients) {
            clientCache[client.getType()] = client
        }
    }

    fun createTeslaConnectionClient(userId: UUID): TeslaConnectionClient {
        val type = userInformationService.getUserInformation(userId).activeTeslaConnectionType
            ?: throw RuntimeException("No active Tesla connection for user: $userId")
        val clientPrototype = clientCache[type] ?: throw RuntimeException("Unknown client type: $type")
        return applicationContext.getBean(clientPrototype::class.java).apply { this.userId = userId }
    }
}