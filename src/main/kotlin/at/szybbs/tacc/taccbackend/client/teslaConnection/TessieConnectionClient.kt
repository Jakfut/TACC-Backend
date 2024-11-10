package at.szybbs.tacc.taccbackend.client.teslaConnection

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaLocation
import at.szybbs.tacc.taccbackend.service.teslaConnections.TessieConnectionService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import java.util.UUID

@Component
@Scope("prototype")
class TessieConnectionClient: TeslaConnectionClient {
    final override lateinit var userId: UUID

    @Autowired
    private lateinit var tessieConnectionService: TessieConnectionService

    private val restClient = RestClient.builder()
        .baseUrl("https://api.tessie.com")
        .defaultHeaders { it.set("Content-Type", "application/json") }
        .build()

    override fun getType(): TeslaConnectionType {
        return TeslaConnectionType.TESSIE
    }

    private val vin: String by lazy { tessieConnectionService.getTeslaConnection(userId).vin.toString() }
    private val token: String by lazy { tessieConnectionService.getTeslaConnection(userId).accessToken.toString() }


    /**
     *  Wakes up the car with the given vin
     *  @return true if the car was successfully woken up
     */
    override fun wake() : Boolean {
        val result = restClient.post()
            .uri("/{vin}/wake", vin)
            .header("Authorization", "Bearer: $token")
            .retrieve()
            .toEntity<String>()

        if (result.statusCode.is4xxClientError) {
            throw Exception("Unauthorized")
            // TODO handle error
        }

        println("Wake:" + result.body)

        return result.statusCode.is2xxSuccessful
    }

    /**
     *  Gets the location of the car with the given vin
     *  @return the location of the car
     */
    override fun getLocation() : TeslaLocation {
        val result = restClient.get()
            .uri("/{vin}/location", vin)
            .header("Authorization", "Bearer: $token")
            .retrieve()
            .toEntity<TeslaLocation>()

        if (!result.statusCode.is2xxSuccessful) {
            throw Exception("Failed to get location")
            // TODO handle error
        }

        println(result.body)

        return result.body ?: throw Exception("Failed to get location")
    }

    override fun getStatus(): String {
        val result = restClient.get()
            .uri("/{vin}/status", vin)
            .header("Authorization", "Bearer: $token")
            .retrieve()
            .toEntity<String>()

        if (!result.statusCode.is2xxSuccessful) {
            throw Exception("Failed to get status")
            // TODO handle error
        }

        val objectMapper = jacksonObjectMapper()
        val status = objectMapper.readValue(result.body, Map::class.java)

        return status["status"] as String
    }

    override fun changeAcState(state: Boolean): Boolean {
        val result = restClient.post()
            .uri("/{vin}/ac", vin)
            .header("Authorization", "Bearer: $token")
            .body(state)
            .retrieve()
            .toEntity<String>()

        if (!result.statusCode.is2xxSuccessful) {
            throw Exception("Failed to change ac state")
            // TODO handle error
        }

        return result.statusCode.is2xxSuccessful
    }
}