package at.szybbs.tacc.taccbackend


import at.szybbs.tacc.taccbackend.dto.calendarConnections.googleCalendar.GoogleCalendarConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie.TessieConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationCreationDto
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import at.szybbs.tacc.taccbackend.repository.calendarConnections.GoogleCalendarConnectionRepository
import at.szybbs.tacc.taccbackend.repository.teslaConnections.TessieConnectionRepository
import at.szybbs.tacc.taccbackend.service.UserInformationService
import at.szybbs.tacc.taccbackend.service.calendarConnections.GoogleCalendarConnectionService
import at.szybbs.tacc.taccbackend.service.teslaConnections.TessieConnectionService
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
import java.util.UUID

@SpringBootTest
class TaccBackendApplicationTests {
	val userId = "e50d926b-f45d-4c0a-9345-0536c04b8162"

	@Autowired
	lateinit var authorizedClientService: JdbcOAuth2AuthorizedClientService

	@Autowired
	lateinit var clientRegistrationRepository: InMemoryClientRegistrationRepository

	@Autowired
	lateinit var authorizedClientManager: AuthorizedClientServiceOAuth2AuthorizedClientManager

	@Autowired
	lateinit var userInformationService: UserInformationService

	@Autowired
	lateinit var tessieConnectionService: TessieConnectionService

	@Autowired
	lateinit var tessieConnectionRepository: TessieConnectionRepository

	@Autowired
	lateinit var googleCalendarConnectionService: GoogleCalendarConnectionService

	@Autowired
	lateinit var googleConnectionRepository: GoogleCalendarConnectionRepository

	@Test
	fun addUserInformation() {
		val userInformationId = UUID.randomUUID()
		val creationDto = UserInformationCreationDto(
			"test",
		)

		userInformationService.createUserInformation(userInformationId, creationDto)
	}

	 @Test
	 fun addTeslaConnection(){
		val userInformationId = UUID.fromString(userId)
		val creationDto = TessieConnectionCreationDto(
			"?",
			"?",
		)

		tessieConnectionService.createTeslaConnection(userInformationId, creationDto)

	 	userInformationService.setActiveTeslaConnectionType(userInformationId, TeslaConnectionType.TESSIE, tessieConnectionRepository)
	 }

	@Test
	fun addGoogleCalendarConnection(){
		val userInformationId = UUID.fromString(userId)
		val creationDto = GoogleCalendarConnectionCreationDto(
			"#start",
			"#end",
		)

		googleCalendarConnectionService.createCalendarConnection(userInformationId, creationDto)

		userInformationService.setActiveCalendarConnectionType(userInformationId, CalendarType.GOOGLE_CALENDAR, googleConnectionRepository)
	}

	@Test
	fun setActiveTeslaConnection(){
		val userInformationId = UUID.fromString(userId)
		userInformationService.setActiveTeslaConnectionType(userInformationId, TeslaConnectionType.MOCK, tessieConnectionRepository)
	}

	@Test
	fun addAuthorizedClient(){
		val clientRegistration: ClientRegistration = clientRegistrationRepository
			.findByRegistrationId("google")

		val accessToken = OAuth2AccessToken(
			OAuth2AccessToken.TokenType.BEARER,
			"",
			Instant.now(),
			Instant.now().plusSeconds(3000)
		)
		val refreshToken = OAuth2RefreshToken(
			"",
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
