package at.szybbs.tacc.taccbackend.client

import org.springframework.http.HttpRequest
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor.PrincipalResolver
import org.springframework.security.oauth2.client.web.client.RequestAttributePrincipalResolver


class TaccPrincipalResolver : PrincipalResolver {
    private val PRINCIPAL_ATTR_NAME = (RequestAttributePrincipalResolver::class.java.name + ".principal")

    override fun resolve(request: HttpRequest?): Authentication {
        return request!!.attributes[PRINCIPAL_ATTR_NAME] as Authentication
    }
}