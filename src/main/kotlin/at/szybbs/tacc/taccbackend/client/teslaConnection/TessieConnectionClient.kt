package at.szybbs.tacc.taccbackend.client.teslaConnection

import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaLocation
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity

class TessieConnectionClient(private val vin: String, private val token: String) {
    private val restClient = RestClient.builder()
        .baseUrl("https://api.tessie.com")
        .defaultHeaders { it.set("Authorization", "Bearer: $token") }
        .defaultHeaders { it.set("Content-Type", "application/json") }
        .build()

    /**
     *  Wakes up the car with the given vin
     *  @return true if the car was successfully woken up
     */
    fun wake() : Boolean {
        val result = restClient.post()
            .uri("/{vin}/wake", vin)
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
    fun getLocation() : TeslaLocation {
        val result = restClient.get()
            .uri("https://api.tessie.com/{vin}/location", vin)
            .retrieve()
            .toEntity<TeslaLocation>()

        if (!result.statusCode.is2xxSuccessful) {
            throw Exception("Failed to get location")
            // TODO handle error
        }

        println(result.body)

        return result.body ?: throw Exception("Failed to get location")
    }
}