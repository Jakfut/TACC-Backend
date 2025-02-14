package at.szybbs.tacc.taccbackend.config

import at.szybbs.tacc.taccbackend.encryption.AesGcmStringEncryptor
import at.szybbs.tacc.taccbackend.encryption.StringEncryptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EncryptionConfig {
    @Value("\${security.encryption.attribute.password}")
    var password: String = ""

    @Bean
    fun encryptor(): StringEncryptor = AesGcmStringEncryptor(password)
}