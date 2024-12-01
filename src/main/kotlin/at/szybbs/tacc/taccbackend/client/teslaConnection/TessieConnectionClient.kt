package at.szybbs.tacc.taccbackend.client.teslaConnection

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaLocation
import at.szybbs.tacc.taccbackend.service.teslaConnections.TessieConnectionService
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import java.util.*


@Component
@Scope("prototype")
class TessieConnectionClient(
    private val tessieConnectionService: TessieConnectionService
): TeslaConnectionClient {
    override lateinit var userId: UUID

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

    /**
     *  Gets the status of the car with the given vin
     *  @return the status of the car
     */

    override fun getStatus(): String {
        val result = restClient.get()
            .uri("/{vin}/status", vin)
            .header("Authorization", "Bearer: $token")
            .retrieve()
            .toEntity(Map::class.java)

        if (!result.statusCode.is2xxSuccessful) {
            throw Exception("Failed to get status")
            // TODO handle error
        }

        // Safely extract the "status" value from the Map
        val status = result.body?.get("status") as? String ?: throw Exception("Status not found")

        println("Status: $status")

        return status
    }

    /**
     *  Changes the ac state of the car with the given vin
     *  @param state the new state of the ac
     *  @return true if the ac state was successfully changed
     */

    override fun changeAcState(state: Boolean): Boolean {
        val endpoint = if (state) "start_climate" else "stop_climate"

        val result = restClient.post()
            .uri("/{vin}/command/$endpoint", vin)
            .header("Authorization", "Bearer: $token")
            .contentLength(0)
            .body(state)
            .retrieve()
            .toEntity<String>()

        if (!result.statusCode.is2xxSuccessful) {
            println(result.body)
            throw Exception("Failed to change ac state")
            // TODO handle error
        }

        println("AC State: $state")

        return result.statusCode.is2xxSuccessful
    }
}