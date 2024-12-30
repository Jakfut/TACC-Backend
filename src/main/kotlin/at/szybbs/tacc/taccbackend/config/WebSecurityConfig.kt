package at.szybbs.tacc.taccbackend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class WebSecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http {
            csrf { // not necessary when using Bearer-Token scheme?!
                disable()
            }
            authorizeHttpRequests {
                // authorize(anyRequest, authenticated) // TODO: activate in production
                authorize(anyRequest, permitAll) // TODO: remove in production
            }
            oauth2ResourceServer {
                jwt {  }
            }
        }

        return http.build()
    }
}