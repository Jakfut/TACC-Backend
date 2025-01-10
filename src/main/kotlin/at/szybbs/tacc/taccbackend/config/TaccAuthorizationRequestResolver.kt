package at.szybbs.tacc.taccbackend.config

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository

class TaccAuthorizationRequestResolver(
    clientRegistrationRepository: ClientRegistrationRepository
) : OAuth2AuthorizationRequestResolver {

    private val defaultResolver = DefaultOAuth2AuthorizationRequestResolver(
        clientRegistrationRepository, "/oauth2/authorization"
    )

    override fun resolve(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        return resolve(request, null)
    }

    override fun resolve(request: HttpServletRequest, clientRegistrationId: String?): OAuth2AuthorizationRequest? {
        val defaultAuthorizationRequest = defaultResolver.resolve(request, clientRegistrationId)
            ?: return null

        val stateFromRequest = request.getParameter("state") ?: throw IllegalArgumentException("Missing state parameter")

        val additionalParameters = mapOf<String, Any>(
            "user_id" to stateFromRequest
        )

        // Build a new authorization request with the original state value
        return OAuth2AuthorizationRequest
            .from(defaultAuthorizationRequest)
            .additionalParameters(additionalParameters)
            .build()
    }
}