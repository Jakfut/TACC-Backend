package at.szybbs.tacc.taccbackend.dto.userInformation

data class UserInformationUpdateDefaultValuesDto(
    val noDestMinutes: Int,
    val ccRuntimeMinutes: Int,
    val arrivalBufferMinutes: Int,
)
