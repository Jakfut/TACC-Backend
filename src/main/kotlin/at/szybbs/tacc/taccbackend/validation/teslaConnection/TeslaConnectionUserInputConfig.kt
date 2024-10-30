package at.szybbs.tacc.taccbackend.validation.teslaConnection

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents user input configurations for TeslaConnections.
 */
class TeslaConnectionUserInputConfig {
    data class TessieConfig (
        @JsonProperty("vin") val vin: String,
        @JsonProperty("access_token") val accessToken: String,
    )
}