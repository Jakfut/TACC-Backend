package at.szybbs.tacc.taccbackend.factory

import at.szybbs.tacc.taccbackend.client.calendarConnection.CalendarConnectionClient
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.service.UserInformationService
import jakarta.annotation.PostConstruct
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import java.util.*

@Service
class CalendarConnectionFactory (
    private val applicationContext: ApplicationContext,
    private val connectionClients: List<CalendarConnectionClient>,
    private var userInformationService: UserInformationService
) {

    private val clientCache = mutableMapOf<CalendarType, CalendarConnectionClient>()

    @PostConstruct
    fun initClientCache() {
        for (client in connectionClients) {
            clientCache[client.getType()] = client
        }
    }

    fun createCalendarConnectionClient(userId: UUID): CalendarConnectionClient {
        val type = userInformationService.getUserInformation(userId).activeCalendarConnectionType
            ?: throw RuntimeException("No active calendar connection for user: $userId")
        val clientPrototype = clientCache[type] ?: throw RuntimeException("Unknown client type: $type")
        return applicationContext.getBean(clientPrototype::class.java).apply { this.userId = userId }
    }
}