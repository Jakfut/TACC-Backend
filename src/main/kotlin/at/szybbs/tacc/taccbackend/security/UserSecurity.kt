package at.szybbs.tacc.taccbackend.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserSecurity {

    @Value("\${security.authentication.roles.create}") private lateinit var createRole : String;

    fun idEqualsAuthenticationId(userInformationId: UUID) : Boolean {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication !is JwtAuthenticationToken) return false

        val authUserInformationId = authentication.name

        return userInformationId == UUID.fromString(authUserInformationId)
    }

    // TODO: use as @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)") on all controller-methods

    fun hasCreateUserRole() : Boolean {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication !is JwtAuthenticationToken) return false

        val realmAccess = authentication.tokenAttributes["realm_access"] as Map<*, *>?

        if (realmAccess == null || realmAccess.isEmpty()) return false

        val roles = realmAccess["roles"] as List<*>?

        if (roles == null || roles.isEmpty()) return false

        return roles.contains(createRole)
    }
}