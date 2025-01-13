package at.szybbs.tacc.taccbackend.service

import at.szybbs.tacc.taccbackend.config.KeycloakConfig
import jakarta.annotation.PostConstruct
import jakarta.ws.rs.core.Response
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class KeycloakAdminApiService (
    private val keycloak: Keycloak,
    @Value("\${security.keycloak.admin-api.realm}")
    private val realmName: String
) {

    private lateinit var realm : RealmResource

    @PostConstruct
    fun init() {
        realm = keycloak.realm(realmName)
    }

    fun userRemovedById(userInformationId: UUID) : Boolean {
        try {
            val response = realm.users().delete(userInformationId.toString())

            return response.statusInfo.family == Response.Status.Family.SUCCESSFUL
        } catch (_ : Exception) {
            return false
        }
    }
}