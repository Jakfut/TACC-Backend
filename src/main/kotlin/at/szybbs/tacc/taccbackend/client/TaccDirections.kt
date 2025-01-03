package at.szybbs.tacc.taccbackend.client

import at.szybbs.tacc.taccbackend.factory.TeslaConnectionFactory
import at.szybbs.tacc.taccbackend.service.userInformation.UserInformationService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.util.UUID

@Component
class TaccDirections(
    private val userInformationService: UserInformationService,
    private val teslaConnectionFactory: TeslaConnectionFactory
) {
    @Value("\${google.maps.api.key}")
    private val key = ""

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     *  Get the drive time in minutes from one location to another
     *  @param from The starting location
     *  @param to The destination location
     *  @return The drive time in minutes or -1 if the request failed
     */

    fun getDriveTimeMinutes(from: String, to: String): Int {
        val result = RestClient.builder()
            .baseUrl("https://maps.googleapis.com/maps/api/directions/json")
            .build()
            .get()
            .uri("?key=$key&origin=$from&destination=$to")
            .exchange{ _, response ->
                if (response.statusCode.is2xxSuccessful) {
                    response.bodyTo(DirectionsResponse::class.java)?.routes?.firstOrNull()?.legs?.firstOrNull()?.duration?.value
                } else {
                    null
                }
            }

        if (result != null) {
            return (result / 60)
        }

        logger.error("Failed to retrieve directions from $from to $to")

        return -1
    }

    /**
     *  Get the drive time in minutes from one location to another with the user's arrival buffer
     *  @param from The starting location
     *  @param to The destination location
     *  @param userId The user's ID
     *  @return The drive time in minutes or -1 if the request failed
     */

    fun getDriveTimeWithVariables(from: String, to: String, userId: UUID): Int {
        val driveTime = getDriveTimeMinutes(from, to)

        if (driveTime == -1) {
            return -1
        }

        return driveTime + userInformationService.getUserInformation(userId).arrivalBufferMinutes
    }

    /**
     *  Get the drive time in minutes from the user's current location to another location
     *  @param to The destination location
     *  @param userId The user's ID
     *  @return The drive time in minutes or -1 if the request failed
     */

    fun getDriveTimeFromCurrentLocationWithVariables(to: String, userId: UUID): Int {
        val userInformation = userInformationService.getUserInformation(userId)

        val teslaConnectionClient = teslaConnectionFactory.createTeslaConnectionClient(
            userId
        )

        val currentLocation = teslaConnectionClient.getLocation()

        return getDriveTimeWithVariables(currentLocation, to, userId)
    }

    private data class DirectionsResponse(
        val routes: List<Route> = emptyList()
    )

    private data class Route(
        val legs: List<Leg> = emptyList()
    )

    private data class Leg(
        val duration: Duration? = null
    )

    private data class Duration(
        val value: Int = 0
    )
}