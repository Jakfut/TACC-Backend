package at.szybbs.tacc.taccbackend.exception.calendarConnection

class InvalidCalendarConnectionConfigFormatException(configFormatClassName: String) :
    RuntimeException("Invalid configuration format for: $configFormatClassName.") {
}