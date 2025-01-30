package at.szybbs.tacc.taccbackend.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.util.UUID

@Service("userSecurity")
class UserSecurityService {

    fun idEqualsAuthenticationId(userInformationId: UUID) : Boolean {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication !is JwtAuthenticationToken) return false

        val authUserInformationId = authentication.name

        return userInformationId == UUID.fromString(authUserInformationId)
    }
}