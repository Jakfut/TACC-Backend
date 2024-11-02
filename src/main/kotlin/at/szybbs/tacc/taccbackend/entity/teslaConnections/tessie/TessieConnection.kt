package at.szybbs.tacc.taccbackend.entity.teslaConnections.tessie

import at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie.TessieConnectionResponseDto
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnection
import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.UUID

@Entity
@EntityListeners(TessieConnectionListener::class)
@Table(name = "tessie_connection")
data class TessieConnection(

    @Id
    @Column(name = "user_information_id", updatable = false, nullable = false)
    override var userInformationId: UUID,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_information_id", insertable=false, updatable=false)
    @JsonIgnore
    override var userInformation: UserInformation? = null,

    @Column(name = "vin")
    var vin: String? = null,

    @Column(name = "access_token")
    var accessToken: String? = null,
) : TeslaConnection {
    override fun toResponseDto(): TessieConnectionResponseDto {
        return TessieConnectionResponseDto(
            userInformationId = userInformationId,
            vin = vin,
            accessToken = accessToken
        )
    }
}

