package at.szybbs.tacc.taccbackend.validation

import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationValidationException
import org.springframework.stereotype.Component

/**
 * Validator for user information.
 *
 * This component validates the fields of a user information entity,
 * ensuring that they conform to the specified constraints.
 */
@Component
class UserInformationValidator {
    fun validate(userInformation: UserInformation) {
        val noDestMinutes = userInformation.noDestMinutes
        if (noDestMinutes < 0 || noDestMinutes > 90)
            throw UserInformationValidationException("noDestMinutes", noDestMinutes.toString())

        val ccRuntimeMinutes = userInformation.ccRuntimeMinutes
        if (ccRuntimeMinutes < 0 || ccRuntimeMinutes > 20)
            throw UserInformationValidationException("ccRuntimeMinutes", ccRuntimeMinutes.toString())

        val arrivalBufferMinutes = userInformation.arrivalBufferMinutes
        if (arrivalBufferMinutes < 0 || arrivalBufferMinutes > 60)
            throw UserInformationValidationException("arrivalBufferMinutes", arrivalBufferMinutes.toString())
    }
}