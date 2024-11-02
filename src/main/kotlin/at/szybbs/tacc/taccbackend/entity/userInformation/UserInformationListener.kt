package at.szybbs.tacc.taccbackend.entity.userInformation

import at.szybbs.tacc.taccbackend.validation.userInformation.UserInformationValidator
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import org.springframework.stereotype.Component

@Component
class UserInformationListener (
    private val validator: UserInformationValidator,
) {

    @PrePersist
    @PreUpdate
    fun validate(userInformation: UserInformation) {
        validator.validate(userInformation)
    }
}