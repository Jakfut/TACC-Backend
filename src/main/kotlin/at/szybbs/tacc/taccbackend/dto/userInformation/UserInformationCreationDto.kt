package at.szybbs.tacc.taccbackend.dto.userInformation

data class UserInformationCreationDto(
    val noDestMinutes: Int,
    val ccRuntimeMinutes: Int,
    val arrivalBufferMinutes: Int,
)
