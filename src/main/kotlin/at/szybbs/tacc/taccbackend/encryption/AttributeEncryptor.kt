package at.szybbs.tacc.taccbackend.encryption

import jakarta.annotation.PostConstruct
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.beans.factory.annotation.Value


@Converter
class AttributeEncryptor(
    @Value("\${security.encryption.attribute.password}") private val password: String,
    @Value("\${security.encryption.attribute.salt}") private val salt: String
) : AttributeConverter<String, String> {
    private lateinit var encryptor: AesCbcEncryptor

    @PostConstruct
    fun init() {
        encryptor = AesCbcEncryptor(password, salt)
    }

    override fun convertToDatabaseColumn(attribute: String?): String? {
        if (attribute == null) return null

        return encryptor.encrypt(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        if (dbData == null) return null

        return encryptor.decrypt(dbData)
    }
}