package at.szybbs.tacc.taccbackend.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "oauth2_authorized_client")
data class OAuth2AuthorizedClient(
    @Id
    @Column(name = "client_registration_id", nullable = false, length = 100)
    val clientRegistrationId: String,

    @Id
    @Column(name = "principal_name", nullable = false, length = 200)
    val principalName: String,

    @Column(name = "access_token_type", nullable = false, length = 100)
    val accessTokenType: String,

    @Lob
    @Column(name = "access_token_value", nullable = false, columnDefinition = "BYTEA")
    val accessTokenValue: ByteArray,

    @Column(name = "access_token_issued_at", nullable = false)
    val accessTokenIssuedAt: Instant,

    @Column(name = "access_token_expires_at", nullable = false)
    val accessTokenExpiresAt: Instant,

    @Column(name = "access_token_scopes", length = 1000)
    val accessTokenScopes: String? = null,

    @Lob
    @Column(name = "refresh_token_value", columnDefinition = "BYTEA")
    val refreshTokenValue: ByteArray? = null,

    @Column(name = "refresh_token_issued_at")
    val refreshTokenIssuedAt: Instant? = null,

    @Column(name = "created_at", updatable = false)
    val createdAt: Instant = Instant.now()
)
