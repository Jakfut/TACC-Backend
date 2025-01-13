package at.szybbs.tacc.taccbackend.config

import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakConfig {

    @Value("\${security.keycloak.admin-api.server-url}")
    lateinit var adminApiServerUrl: String

    @Value("\${security.keycloak.admin-api.realm}")
    lateinit var adminApiRealm: String

    @Value("\${security.keycloak.admin-api.client-id}")
    private lateinit var adminApiClientId: String

    @Value("\${security.keycloak.admin-api.client-secret}")
    private lateinit var adminApiClientSecret: String

    @Bean
    fun keycloak() : Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(adminApiServerUrl)
            .realm(adminApiRealm)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId(adminApiClientId)
            .clientSecret(adminApiClientSecret)
            .build();
    }
}