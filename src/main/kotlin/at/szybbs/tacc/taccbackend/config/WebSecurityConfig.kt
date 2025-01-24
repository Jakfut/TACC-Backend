package at.szybbs.tacc.taccbackend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Bean
    fun filterChain(
        http: HttpSecurity,
        clientRegistrationRepository: ClientRegistrationRepository,
        taccOAuth2GrantFilter: TaccOAuth2GrantFilter
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
            }
            oauth2ResourceServer {
                jwt { }
            }
            addFilterBefore<OAuth2LoginAuthenticationFilter>(taccOAuth2GrantFilter)
        }
        return http.build()
    }
}