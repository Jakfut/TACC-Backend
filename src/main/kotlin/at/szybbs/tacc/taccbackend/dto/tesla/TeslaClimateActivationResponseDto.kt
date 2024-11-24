package at.szybbs.tacc.taccbackend.dto.tesla

import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

data class TeslaClimateActivationResponseDto(
    val climateActivationTime: ZonedDateTime,
    val departureTime: ZonedDateTime,
    val arrivalTime: ZonedDateTime,
    val eventStartTime: ZonedDateTime,
    val eventLocation: String,
) {
    val travelTimeMinutes: Long = ChronoUnit.MINUTES.between(departureTime, arrivalTime);
}
