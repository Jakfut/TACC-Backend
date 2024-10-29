package at.szybbs.tacc.taccbackend.validation.calendarConnection

import at.szybbs.tacc.taccbackend.exception.calendarConnection.IllegalCalendarConnectionConfigValueException
import at.szybbs.tacc.taccbackend.exception.calendarConnection.InvalidCalendarConfigFormatException
import at.szybbs.tacc.taccbackend.exception.calendarConnection.UnsupportedCalendarTypeException
import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

/**
 * Validator and mapper for user input configurations of calendar connections.
 */
@Component
class CalendarConnectionUserInputConfigValidatorMapper (
    private val objectMapper: ObjectMapper
) {

    /**
     * Validates and maps the given configuration based on the specified calendar type.
     *
     * @param calendarType The type of calendar (e.g., Google Calendar) for which the config is validated
     * @param config The configuration data as a JsonNode to be validated and mapped
     * @return A validated and mapped JsonNode configuration
     * @throws UnsupportedCalendarTypeException if the provided calendar type is not supported
     * @throws InvalidCalendarConfigFormatException if the config format is invalid
     * @throws IllegalCalendarConnectionConfigValueException if any configuration values are illegal
     */
    fun validateAndMap(calendarType: CalendarType, config: JsonNode) : JsonNode {
        if (calendarType == CalendarType.GOOGLE_CALENDAR) return validateAndMapGoogleCalendarConfig(config)

        throw UnsupportedCalendarTypeException(calendarType, "validating CalendarConnectionUserInputConfig")
    }

    /**
     * Validates and maps the Google Calendar configuration.
     *
     * @param config The configuration data as a JsonNode specific to Google Calendar
     * @return A validated and mapped JsonNode configuration for Google Calendar
     * @throws InvalidCalendarConfigFormatException if the config format is invalid
     * @throws IllegalCalendarConnectionConfigValueException if any configuration values are illegal
     */
    private fun validateAndMapGoogleCalendarConfig(config: JsonNode) : JsonNode {
        val googleCalendarConfig: CalendarConnectionUserInputConfig.GoogleCalendarConfig

        try {
            googleCalendarConfig = objectMapper.convertValue(config, CalendarConnectionUserInputConfig.GoogleCalendarConfig::class.java)
        } catch (e: IllegalArgumentException) {
            throw InvalidCalendarConfigFormatException("CalendarConnectionUserInputConfig.GoogleCalendarConfig")
        }

        val keyword = googleCalendarConfig.keyword
        val keywordRegex = CalendarConnectionConfigValidationRegexPatterns.GOOGLE_CALENDAR_KEYWORD_REGEX
        if (!keyword.matches(keywordRegex)) throw IllegalCalendarConnectionConfigValueException(
            configField = "keyword",
            configValue = keyword
        )

        return objectMapper.valueToTree(googleCalendarConfig)
    }
}