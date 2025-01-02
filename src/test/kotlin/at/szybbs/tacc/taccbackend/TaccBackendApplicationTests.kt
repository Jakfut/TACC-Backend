package at.szybbs.tacc.taccbackend


import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2RefreshToken
import java.time.Instant

@SpringBootTest
class TaccBackendApplicationTests {
	val userId = "409cd7c7-e82d-406c-a784-621598ff45e9"

	@Autowired
	lateinit var authorizedClientService: JdbcOAuth2AuthorizedClientService

	@Autowired
	lateinit var clientRegistrationRepository: InMemoryClientRegistrationRepository

	@Autowired
	lateinit var authorizedClientManager: AuthorizedClientServiceOAuth2AuthorizedClientManager

	@Test
	fun addAuthorizedClient(){
		val clientRegistration: ClientRegistration = clientRegistrationRepository
			.findByRegistrationId("google")

		val accessToken = OAuth2AccessToken(
			OAuth2AccessToken.TokenType.BEARER,
			"ya29.a0ARW5m74PS5ztGdvmls_adeRbGlIYtCVqKj71Se2HQg5_SUqs1gbT7EChKwHxEmkuJPAXxAAhOL6xvsi9fwa06aBGBTT8tlmAO1_2u4fzimHPGuVpJ2Wp0ZxunHEcyqpRBZo7aEMRwmpEmMcwSymtykYhKdBsplA4BZHdiceqaCgYKASESARASFQHGX2MiihEv3fGOwOK6KOjrCX9YNQ0175",
			Instant.now(),
			Instant.now().plusSeconds(3000)
		)
		val refreshToken = OAuth2RefreshToken(
			"1//04YT-S7pjRZ1FCgYIARAAGAQSNwF-L9IrbOu-F5jhdRgVRWUaiKjdMLt69XGD08spDDsZSarbTjSGxo7fE59R8nidTDZWScbOke4",
			Instant.now()
		)

		val authorizedClient = OAuth2AuthorizedClient(
			clientRegistration,
			userId,
			accessToken,
			refreshToken
		)

		val authentication = UsernamePasswordAuthenticationToken(
			userId,
			null
		)

		authorizedClientService.saveAuthorizedClient(authorizedClient, authentication)
	}

	@Test
	fun getAccessTokenValue(){
		val authorizedClient = authorizedClientService.loadAuthorizedClient<OAuth2AuthorizedClient>(
			"google",
			userId
		)

		println(authorizedClient.accessToken.tokenValue)
	}

	@Test
	fun getAccessTokenExpire(){
		val authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("google")
			.principal(userId)
			.build()
		val authorizedClient = authorizedClientManager.authorize(authorizeRequest)

		if (authorizedClient != null) {
			println(authorizedClient.accessToken.expiresAt)
		}
	}
}
