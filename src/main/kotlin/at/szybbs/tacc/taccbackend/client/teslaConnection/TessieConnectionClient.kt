package at.szybbs.tacc.taccbackend.client.teslaConnection

import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnectionType
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaLocation
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity

@Component
@Scope("prototype")
class TessieConnectionClient: TeslaConnectionClient {
    override lateinit var vin: String
    override lateinit var token: String

    private val restClient = RestClient.builder()
        .baseUrl("https://api.tessie.com")
        .defaultHeaders { it.set("Content-Type", "application/json") }
        .build()

    override fun getType(): TeslaConnectionType {
        return TeslaConnectionType.TESSIE
    }

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
        TODO("Not yet implemented")
    }
}