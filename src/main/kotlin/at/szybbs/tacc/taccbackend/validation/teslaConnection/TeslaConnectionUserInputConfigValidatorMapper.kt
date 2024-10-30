package at.szybbs.tacc.taccbackend.validation.teslaConnection

import at.szybbs.tacc.taccbackend.exception.teslaConnection.InvalidTeslaConnectionConfigFormatException
import at.szybbs.tacc.taccbackend.exception.teslaConnection.UnsupportedTeslaConnectionTypeException
import at.szybbs.tacc.taccbackend.exception.teslaConnection.IllegalTeslaConnectionConfigValueException
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnectionType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

/**
 * Validator and mapper for user input configurations of TeslaConnections.
 */
@Component
class TeslaConnectionUserInputConfigValidatorMapper (
    private val objectMapper: ObjectMapper
) {

    /**
     * Validates and maps the given configuration based on the specified TeslaConnectionType.
     *
     * @param teslaConnectionType The type of TeslaConnection (e.g., Tessie) for which the config is validated
     * @param config The configuration data as a JsonNode to be validated and mapped
     * @return A validated and mapped JsonNode configuration
     * @throws UnsupportedTeslaConnectionTypeException if the provided TeslaConnectionType is not supported
     * @throws InvalidTeslaConnectionConfigFormatException if the config format is invalid
     * @throws IllegalTeslaConnectionConfigValueException if any configuration values are illegal
     */
    fun validateAndMap(teslaConnectionType: TeslaConnectionType, config: JsonNode) : JsonNode {
        if (teslaConnectionType == TeslaConnectionType.TESSIE) return validateAndMapTessieConfig(config)

        throw UnsupportedTeslaConnectionTypeException(teslaConnectionType, "validating TeslaConnectionUserInputConfig")
    }

    /**
     * Validates and maps the Tessie configuration.
     *
     * @param config The configuration data as a JsonNode specific to Tessie
     * @return A validated and mapped JsonNode configuration for Tessie
     * @throws InvalidTeslaConnectionConfigFormatException if the config format is invalid
     * @throws IllegalTeslaConnectionConfigValueException if any configuration values are illegal
     */
    private fun validateAndMapTessieConfig(config: JsonNode) : JsonNode {
        val tessieConfig: TeslaConnectionUserInputConfig.TessieConfig

        try {
            tessieConfig = objectMapper.convertValue(config, TeslaConnectionUserInputConfig.TessieConfig::class.java)
        } catch (e: IllegalArgumentException) {
            throw InvalidTeslaConnectionConfigFormatException("TeslaConnectionUserInputConfig.TessieConfig")
        }

        return objectMapper.valueToTree(tessieConfig)
    }
}