package at.szybbs.tacc.taccbackend.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserSecurity {

    fun idEqualsAuthenticationId(userInformationId: UUID) : Boolean {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication !is JwtAuthenticationToken) return false

        val authUserInformationId = authentication.name

        return userInformationId == UUID.fromString(authUserInformationId)
    }
}