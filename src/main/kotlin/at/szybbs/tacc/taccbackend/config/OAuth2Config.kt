package at.szybbs.tacc.taccbackend.config

import at.szybbs.tacc.taccbackend.service.userInformation.UserInformationService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.UUID
import javax.sql.DataSource

@Configuration
class OAuth2Config(
    private val dataSource: DataSource,
    private val userInformationService: UserInformationService
) {
    @Bean
    fun jdbcOperations(): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }

    @Bean
    fun authorizedClientService(
        jdbcOperations: JdbcOperations,
        authorizedClientRepository: ClientRegistrationRepository
    ): JdbcOAuth2AuthorizedClientService {
        return JdbcOAuth2AuthorizedClientService(
            jdbcOperations,
            authorizedClientRepository
        )
    }

    @Bean
    fun authorizedClientServiceOAuth2AuthorizedClientManager(
        authorizedClientRepository: ClientRegistrationRepository,
        authorizedClientService: JdbcOAuth2AuthorizedClientService
    ): AuthorizedClientServiceOAuth2AuthorizedClientManager {
        val authorizedClientServiceOAuth2AuthorizedClientManager = AuthorizedClientServiceOAuth2AuthorizedClientManager(
            authorizedClientRepository,
            authorizedClientService
        )
        authorizedClientServiceOAuth2AuthorizedClientManager.setAuthorizedClientProvider(
            OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .build()
        )

        return authorizedClientServiceOAuth2AuthorizedClientManager
    }

    @Bean
    fun oauth2UserService(): OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        return object : DefaultOAuth2UserService() {
            override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
                val oAuth2User = super.loadUser(userRequest)

                val userId = userRequest.additionalParameters["user_id"] as? UUID
                    ?: throw RuntimeException("User ID not found in additional parameters")

                val user = userInformationService.getUserInformation(userId)

                val authorities = listOf(SimpleGrantedAuthority("ROLE_USER")) // Example authority
                return DefaultOAuth2User(
                    authorities,
                    oAuth2User.attributes + mapOf("app_user_id" to user.id), // Add your user_id as a custom attribute and keep the other attributes
                    "app_user_id" // Set the name attribute to your custom attribute name
                )
            }
        }
    }

    @Bean
    fun oauth2AuthorizationRequestResolver(
        clientRegistrationRepository: ClientRegistrationRepository
    ): OAuth2AuthorizationRequestResolver {
        return TaccAuthorizationRequestResolver(clientRegistrationRepository)
    }
}