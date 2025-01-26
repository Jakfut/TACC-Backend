package at.szybbs.tacc.taccbackend.config

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import java.util.*

class TaccAuthorizationRequestResolver(
    clientRegistrationRepository: ClientRegistrationRepository
) : OAuth2AuthorizationRequestResolver {

    private val defaultResolver = DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization")

    private val logger = LoggerFactory.getLogger(TaccAuthorizationRequestResolver::class.java)

    override fun resolve(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        val defaultAuthorizationRequest = defaultResolver.resolve(request)
        if (defaultAuthorizationRequest != null) {
            val sessionId = request.getParameter("session_id")
            if (!sessionId.isNullOrEmpty()) {
                val newState = Base64.getEncoder().encode((defaultAuthorizationRequest.state + ";session_id=$sessionId").toByteArray())

                logger.warn("Client provided sessionId: $sessionId")
                logger.warn("Old state: ${defaultAuthorizationRequest.state}")
                logger.warn("New state: ${String(newState)}")

                return OAuth2AuthorizationRequest.from(defaultAuthorizationRequest)
                    .state(String(newState))
                    .additionalParameters(defaultAuthorizationRequest.additionalParameters + mapOf("access_type" to "offline"))
                    .build()
            }
        }
        return defaultAuthorizationRequest
    }

    override fun resolve(request: HttpServletRequest, clientRegistrationId: String): OAuth2AuthorizationRequest? {
        throw UnsupportedOperationException("This resolver only supports resolving from the initial request")
    }
}