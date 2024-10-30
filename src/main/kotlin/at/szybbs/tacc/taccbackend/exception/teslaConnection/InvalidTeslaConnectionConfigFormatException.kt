package at.szybbs.tacc.taccbackend.exception.teslaConnection

class InvalidTeslaConnectionConfigFormatException(configFormatClassName: String) :
    RuntimeException("Invalid configuration format for: $configFormatClassName.") {
}