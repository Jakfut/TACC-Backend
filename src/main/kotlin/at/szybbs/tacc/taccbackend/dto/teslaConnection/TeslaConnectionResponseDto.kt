package at.szybbs.tacc.taccbackend.dto.teslaConnection

import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnection
import at.szybbs.tacc.taccbackend.model.teslaConnection.TeslaConnectionType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import java.util.UUID

data class TeslaConnectionResponseDto(
    @JsonProperty("user_information_id") var userInformationId: UUID,
    @JsonProperty("tesla_connection_type") var teslaConnectionType: TeslaConnectionType,
    @JsonProperty("active") var active: Boolean,
    @JsonProperty("config") var config: JsonNode
)

fun TeslaConnection.toResponseDto(): TeslaConnectionResponseDto {
    return TeslaConnectionResponseDto(
        userInformationId = this.id.userInformationId,
        teslaConnectionType = this.id.type,
        active = this.active,
        config = this.config,
    )
}

fun List<TeslaConnection>.toResponseDto(): List<TeslaConnectionResponseDto> {
    return this.map { it.toResponseDto() }
}