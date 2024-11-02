package at.szybbs.tacc.taccbackend.entity.teslaConnections.tessie

import at.szybbs.tacc.taccbackend.validation.teslaConnections.TessieConnectionValidator
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import org.springframework.stereotype.Component

@Component
class TessieConnectionListener (
    private val validator: TessieConnectionValidator
) {

    @PrePersist
    @PreUpdate
    fun validate(tessieConnection: TessieConnection) {
        validator.validate(tessieConnection)
    }

    /**
     * TODO: implement encryption/decryption
     */
}