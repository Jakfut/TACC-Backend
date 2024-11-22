package at.szybbs.tacc.taccbackend.encryption

import jakarta.annotation.PostConstruct
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.beans.factory.annotation.Value


@Converter
class AttributeEncryptor : AttributeConverter<String, String> {

    @Value("\${encryption.attribute.password}") private lateinit var password : String
    @Value("\${encryption.attribute.salt}") private lateinit var salt : String

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