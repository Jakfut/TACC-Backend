package at.szybbs.tacc.taccbackend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.user.OAuth2User

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity, oauth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>,
                    oauth2AuthorizationRequestResolver: OAuth2AuthorizationRequestResolver
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

                /*userInfoEndpoint {
                    userService = oauth2UserService
                }

                authorizationEndpoint {
                    authorizationRequestResolver = oauth2AuthorizationRequestResolver
                }*/
            }
        }
        return http.build()
    }
}