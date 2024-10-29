package at.szybbs.tacc.taccbackend.exception.calendarConnection

class InvalidCalendarConfigFormatException(configFormatClassName: String) :
    RuntimeException("Invalid configuration format for: $configFormatClassName.") {
}