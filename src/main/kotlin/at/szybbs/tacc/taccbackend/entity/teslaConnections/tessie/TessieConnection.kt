package at.szybbs.tacc.taccbackend.entity.teslaConnections.tessie

import at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie.TessieConnectionResponseDto
import at.szybbs.tacc.taccbackend.encryption.AttributeEncryptor
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnection
import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.UUID

@Entity
@EntityListeners(TessieConnectionListener::class)
@Table(name = "tessie_connection")
data class TessieConnection(

    @Id
    @Column(name = "user_information_id", updatable = false, nullable = false)
    override var userInformationId: UUID,

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId("userInformationId")
    @JoinColumn(name = "user_information_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    override var userInformation: UserInformation? = null,

    @Convert(converter = AttributeEncryptor::class)
    @Column(name = "vin", nullable = false, columnDefinition = "TEXT")
    var vin: String,

    @Convert(converter = AttributeEncryptor::class)
    @Column(name = "access_token", nullable = false, columnDefinition = "TEXT")
    var accessToken: String,

) : TeslaConnection {
    override fun toResponseDto(): TessieConnectionResponseDto {
        return TessieConnectionResponseDto(
            userInformationId = userInformationId,
            vin = vin,
            accessToken = accessToken
        )
    }
}

