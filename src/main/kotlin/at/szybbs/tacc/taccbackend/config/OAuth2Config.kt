package at.szybbs.tacc.taccbackend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import javax.sql.DataSource

@Configuration
class OAuth2Config(
    private val dataSource: DataSource,
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
        return DefaultOAuth2UserService()
    }
}