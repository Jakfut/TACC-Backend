package at.szybbs.tacc.taccbackend.encryption

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class AttributeEncryptionConverter (
    private val encryptor: StringEncryptor
) : AttributeConverter<String, String> {

    override fun convertToDatabaseColumn(attribute: String?): String? {
        if (attribute == null) return null

        return encryptor.encrypt(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        if (dbData == null) return null

        return encryptor.decrypt(dbData)
    }
}