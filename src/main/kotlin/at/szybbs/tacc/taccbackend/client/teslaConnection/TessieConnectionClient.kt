package at.szybbs.tacc.taccbackend.client.teslaConnection

import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import at.szybbs.tacc.taccbackend.service.teslaConnections.TessieConnectionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val restClient = RestClient.builder()
        .baseUrl("https://api.tessie.com")
        .defaultHeaders { it.set("Content-Type", "application/json") }
        .build()

    override fun getType(): TeslaConnectionType {
        return TeslaConnectionType.TESSIE
    }

    private val vin: String by lazy { tessieConnectionService.getTeslaConnection(userId).vin }
    private val token: String by lazy { tessieConnectionService.getTeslaConnection(userId).accessToken }

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
            logger.warn("Failed to wake up car with vin: $vin, Body: ${result.body}")
        }

        logger.info("Woke up car with vin: $vin")

        return result.statusCode.is2xxSuccessful
    }

    /**
     *  Gets the location of the car with the given vin
     *  @return the location of the car
     */

    override fun getLocation() : String {
        val result = restClient.get()
            .uri("/{vin}/location", vin)
            .header("Authorization", "Bearer: $token")
            .retrieve()
            .toEntity(Map::class.java)

        if (!result.statusCode.is2xxSuccessful) {
            logger.warn("Failed to get address of car with vin: $vin, Body: ${result.body}")
        }

        // Safely extract the "address" value from the Map
        return result.body?.get("address") as? String ?: throw Exception("Failed to get location")
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
            logger.warn("Failed to get status of car with vin: $vin, Body: ${result.body}")
        }

        // Safely extract the "status" value from the Map
        return result.body?.get("status") as? String ?: throw Exception("Failed to get status")
    }

    /**
     *  Gets the ac state of the car with the given vin
     *  @return the ac state of the car
     */

    override fun getAcStatus(): Boolean {
        val result = restClient.get()
            .uri("/{vin}/state", vin)
            .header("Authorization", "Bearer: $token")
            .retrieve()
            .toEntity(Map::class.java)

        if (!result.statusCode.is2xxSuccessful) {
            logger.warn("Failed to get ac state of car with vin: $vin, Body: ${result.body}")
        }

        // Safely extract the "ac state" value from the Map
        val climateState = result.body?.get("climate_state") as? Map<*, *>
        val isClimateOn = climateState?.get("is_climate_on") as? Boolean ?: false
        return isClimateOn
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
            logger.warn("Failed to change AC state of car with vin: $vin, Body: ${result.body}")
        }

        logger.info("Changed AC state of car with vin: $vin to $state")

        return result.statusCode.is2xxSuccessful
    }

    override fun isUserPresent(): Boolean {
        val result = restClient.get()
            .uri("/{vin}/state", vin)
            .header("Authorization", "Bearer: $token")
            .retrieve()
            .toEntity(Map::class.java)

        if (!result.statusCode.is2xxSuccessful) {
            logger.warn("Failed to get is_user_present of car with vin: $vin, Body: ${result.body}")
        }

        // Safely extract the "ac state" value from the Map
        val vehicleState = result.body?.get("vehicle_state") as? Map<*, *>
        val isUserPresent = vehicleState?.get("is_user_present") as? Boolean ?: false
        return isUserPresent
    }

    override fun testConnection(vin: String, token: String): Boolean {
        val result = restClient.get()
            .uri("/{vin}/status", vin)
            .header("Authorization", "Bearer: $token")
            .retrieve()
            .toEntity<Map<String, String>>()

        return result.statusCode.is2xxSuccessful
    }
}