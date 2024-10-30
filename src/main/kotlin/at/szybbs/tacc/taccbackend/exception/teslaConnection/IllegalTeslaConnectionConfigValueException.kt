package at.szybbs.tacc.taccbackend.exception.teslaConnection

class IllegalTeslaConnectionConfigValueException(
    configField: String,
    configValue: String
) : RuntimeException("Illegal value: '$configValue' for field: '$configField' for TeslaConnection configuration.") {
}
