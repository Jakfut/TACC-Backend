package at.szybbs.tacc.taccbackend.controller

import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/*
@Controller
class AuthController(
    private val oauth2AuthorizationRequestResolver: OAuth2AuthorizationRequestResolver
) {


    @GetMapping("/auth/google/start")
    fun handleGoogleRedirect(): String {
        val generatedSession = "TestSession"

        // get the authorization request for registration google
        val authorizationRequest = oauth2AuthorizationRequestResolver.resolve(null, "google")

        return ""
    }
}*/
