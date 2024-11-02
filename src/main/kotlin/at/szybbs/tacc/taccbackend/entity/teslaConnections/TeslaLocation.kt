package at.szybbs.tacc.taccbackend.model.teslaConnection

import com.fasterxml.jackson.annotation.JsonProperty

data class TeslaLocation(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    @JsonProperty("saved_location")
    val savedLocation: String
)   {
    override fun toString(): String {
        return "latitude: $latitude \nlongitude: $longitude \naddress: $address \nsaved_location: $savedLocation"
    }

    fun getDriveTime(tarLocation: TeslaLocation) : Double {
        // TODO implement
        return 0.0
    }
}