package at.szybbs.tacc.taccbackend.entity.teslaConnections

/**
 * Enumeration of supported external TeslaConnection connection service providers.
 *
 * - [TESSIE]: Represents Tessie as a service provider.
 *
 * Additional providers can be added as needed.
 */
enum class TeslaConnectionType {
    TESSIE,
    MOCK,
}