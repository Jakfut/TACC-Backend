package at.szybbs.tacc.taccbackend.exception.calendarConnection

class IllegalCalendarConnectionConfigValueException(
    configField: String,
    configValue: String
) : RuntimeException("Illegal value: '$configValue' for field: '$configField' for calendar connection configuration.") {
}
