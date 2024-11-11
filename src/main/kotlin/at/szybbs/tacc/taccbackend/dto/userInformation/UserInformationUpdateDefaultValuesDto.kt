package at.szybbs.tacc.taccbackend.dto.userInformation

import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation

data class UserInformationUpdateDefaultValuesDto(
    val noDestMinutes: Int,
    val ccRuntimeMinutes: Int,
    val arrivalBufferMinutes: Int,
) {
    fun hasChanged(userInformation: UserInformation): Boolean {
        return userInformation.noDestMinutes != noDestMinutes ||
                userInformation.ccRuntimeMinutes != ccRuntimeMinutes ||
                userInformation.arrivalBufferMinutes != arrivalBufferMinutes
    }
}
