package at.szybbs.tacc.taccbackend.validation.calendarConnection

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents user input configurations for CalendarConnections.
 */
class CalendarConnectionUserInputConfig {
    data class GoogleCalendarConfig (
        @JsonProperty("keyword") val keyword: String,
    )
}