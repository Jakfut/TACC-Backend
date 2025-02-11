package at.szybbs.tacc.taccbackend.config

import at.szybbs.tacc.taccbackend.client.calendarConnection.getGoogleCalendarEmail
import at.szybbs.tacc.taccbackend.service.UserInformationService
import at.szybbs.tacc.taccbackend.service.calendarConnections.GoogleCalendarConnectionService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.ApplicationContext
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.client.endpoint.RestClientAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.web.util.UrlUtils
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@Component
class TaccOAuth2GrantFilter(
    private val clientRegistrationRepository: ClientRegistrationRepository,
    private val authorizedClientService: JdbcOAuth2AuthorizedClientService,
    private val userInformationService: UserInformationService,
    private val googleCalendarConnectionService: GoogleCalendarConnectionService,
    private val applicationContext: ApplicationContext
) : OncePerRequestFilter() {
    private val oauth2AccessTokenResponseClient: OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> = RestClientAuthorizationCodeTokenResponseClient()
    private val authorizationRequestRepository: AuthorizationRequestRepository<OAuth2AuthorizationRequest> = HttpSessionOAuth2AuthorizationRequestRepository()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath == "/authorized/google") {
            val params = LinkedMultiValueMap<String, String>().apply {
                request.parameterMap.forEach { (key, values) ->
                    values.filterNotNull().forEach { value ->
                        add(key, value)
                    }
                }
            }

            val authorizationRequest = this.authorizationRequestRepository.removeAuthorizationRequest(request, response)
            val registrationId = authorizationRequest.getAttribute<String>(OAuth2ParameterNames.REGISTRATION_ID)
            val clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId)

            if (clientRegistration == null) {
                val oauth2Error = OAuth2Error(
                    OAuth2ErrorCodes.INVALID_CLIENT,
                    "Client Registration not found with Id: $registrationId", null
                )
                throw OAuth2AuthenticationException(oauth2Error, oauth2Error.toString())
            }

            val redirectUri = UriComponentsBuilder.fromUriString(UrlUtils.buildFullRequestUrl(request))
                .replaceQuery(null)
                .build()
                .toUriString()

            val code = params.getFirst(OAuth2ParameterNames.CODE)
            val state = params.getFirst(OAuth2ParameterNames.STATE)

            val authorizationResponse = if (code != null) {
                OAuth2AuthorizationResponse.success(code)
                    .redirectUri(redirectUri)
                    .state(state)
                    .build()
            } else {
                val errorDescription = params.getFirst(OAuth2ParameterNames.ERROR_DESCRIPTION)
                val errorUri = params.getFirst(OAuth2ParameterNames.ERROR_URI)
                val errorCode = params.getFirst(OAuth2ParameterNames.ERROR)
                OAuth2AuthorizationResponse.error(errorCode ?: "unknown_error")
                    .redirectUri(redirectUri)
                    .errorDescription(errorDescription)
                    .errorUri(errorUri)
                    .state(state)
                    .build()
            }

            val authorizationExchange = OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse)

            logger.warn("Code: $code")

            val tokenResponse = oauth2AccessTokenResponseClient.getTokenResponse(
                OAuth2AuthorizationCodeGrantRequest(
                    clientRegistration,
                    authorizationExchange,
                )
            )

            // decode state
            val decoder = Base64.getDecoder()
            val decodedState = String(decoder.decode(state))
            val parts = decodedState.split(";session_id=")
            val sessionId = parts[1]

            // get user with session id
            val userId = userInformationService.getUserIdBySession(sessionId) ?: return
            val calendarConnection = googleCalendarConnectionService.getCalendarConnection(userId)

            val authorizedClient = OAuth2AuthorizedClient(
                clientRegistration,
                calendarConnection.oauth2ConnectionId,
                tokenResponse.accessToken,
                tokenResponse.refreshToken
            )

            val authentication = UsernamePasswordAuthenticationToken(
                calendarConnection.oauth2ConnectionId,
                null
            )

            authorizedClientService.saveAuthorizedClient(
                authorizedClient,
                authentication
            )

            val email = getGoogleCalendarEmail(userId, applicationContext)

            googleCalendarConnectionService.setGoogleCalendarEmail(userId, email)

            return response.sendRedirect("/authorized/google")
        }

        filterChain.doFilter(request, response)
    }
}