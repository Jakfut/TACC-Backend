package at.szybbs.tacc.taccbackend.dto.userInformation

import com.fasterxml.jackson.annotation.JsonProperty

data class UserInformationCreationDto(
    @JsonProperty("no_dest_minutes") val noDestMinutes: Int,
    @JsonProperty("cc_runtime_minutes") val ccRuntimeMinutes: Int,
    @JsonProperty("arrival_buffer_minutes") val arrivalBufferMinutes: Int,
)
