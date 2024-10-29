package at.szybbs.tacc.taccbackend.validation.calendarConnection

/**
 * Object containing regular expression patterns for validating calendar connection configurations.
 */
object CalendarConnectionConfigValidationRegexPatterns {
    val GOOGLE_CALENDAR_KEYWORD_REGEX = Regex("^#[A-Za-z0-9]{3,5}$")
}