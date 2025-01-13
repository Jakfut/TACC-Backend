package at.szybbs.tacc.taccbackend.config

import at.szybbs.tacc.taccbackend.service.userInformation.UserInformationService
import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.client.endpoint.RestClientAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component


@Component
class TaccOAuth2AuthenticationProvider(
    private val userInformationService: UserInformationService,
    private val httpSession: HttpSession,
    private val delegate: OAuth2UserService<OAuth2UserRequest, OAuth2User>,
    private val clientRegistrationRepository: ClientRegistrationRepository
) : AuthenticationProvider {
    private val logger = LoggerFactory.getLogger(TaccOAuth2AuthenticationProvider::class.java)
    private val accessTokenResponseClient: OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> = RestClientAuthorizationCodeTokenResponseClient()



    override fun authenticate(authentication: Authentication) : Authentication?
    {
        return OAuth2LoginAuthenticationToken(
            clientRegistrationRepository.findByRegistrationId("google"),
            null,
            DefaultOAuth2User(
                null,
                mapOf("testUser" to "test"),
                "testUser"
            ),
            authentication.authorities,
            OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "testToken", null, null),
            null
        )
    }

    override fun supports(authentication: Class<*>): Boolean {
        return OAuth2LoginAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}