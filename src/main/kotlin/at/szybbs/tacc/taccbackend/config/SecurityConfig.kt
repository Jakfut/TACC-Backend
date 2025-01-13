package at.szybbs.tacc.taccbackend.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationProvider
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.RestClientAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(
        http: HttpSecurity,
        clientRegistrationRepository: ClientRegistrationRepository,
        stateDecodingFilter: StateDecodingFilter,
        authenticationProvider: AuthenticationProvider,
    ): SecurityFilterChain {
        http {
            csrf { // not necessary when using Bearer-Token scheme?!
                disable()
            }
            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }

            oauth2Login {
                loginProcessingUrl = "/authorized/*"

                authorizationEndpoint {
                    authorizationRequestResolver = TaccAuthorizationRequestResolver(clientRegistrationRepository)
                }

                //http.authenticationProvider(authenticationProvider)

                /*userInfoEndpoint {
                    userService = oauth2UserService
                }

                authorizationEndpoint {
                    authorizationRequestResolver = oauth2AuthorizationRequestResolver
                }*/
            }
            addFilterBefore<OAuth2LoginAuthenticationFilter>(stateDecodingFilter)
        }
        return http.build()
    }
}