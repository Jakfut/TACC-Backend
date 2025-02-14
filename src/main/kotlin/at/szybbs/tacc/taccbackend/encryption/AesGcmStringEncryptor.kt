package at.szybbs.tacc.taccbackend.encryption

import org.slf4j.LoggerFactory
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.text.toByteArray

class AesGcmStringEncryptor(
    masterPassword: String,
) : StringEncryptor {
    private companion object {
        private const val AES_KEY_SIZE = 256 // in bits
        private const val GCM_NONCE_LENGTH = 12 // in bytes
        private const val GCM_TAG_LENGTH = 128 // in bits
        private const val AES_ALGORITHM = "AES/GCM/NoPadding"
    }

    private val logger = LoggerFactory.getLogger(javaClass)
    private val secretKey : SecretKeySpec = run {
        val digest = MessageDigest.getInstance("SHA-$AES_KEY_SIZE")
        SecretKeySpec(digest.digest(masterPassword.toByteArray()), "AES")
    }

    private fun generateGCMParameter(): GCMParameterSpec {
        val nonce = ByteArray(GCM_NONCE_LENGTH)
        SecureRandom().nextBytes(nonce)
        return GCMParameterSpec(GCM_TAG_LENGTH, nonce)
    }

    private fun generateGCMParameter(nonce: ByteArray): GCMParameterSpec {
        if (nonce.size != GCM_NONCE_LENGTH) throw IllegalArgumentException("Nonce must be $GCM_NONCE_LENGTH bytes")

        return GCMParameterSpec(GCM_TAG_LENGTH, nonce)
    }

    override fun encrypt(plainText: String): String {
        if (plainText.isEmpty()) return ""

        runCatching {
            val gcmSpec = generateGCMParameter()

            val cipher = Cipher.getInstance(AES_ALGORITHM)

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec)
            val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

            return Base64.getEncoder().encodeToString(gcmSpec.iv + cipherText)
        }.getOrElse {
            logger.error("Unable to encrypt data")
            throw it
        }
    }

    override fun decrypt(cipherText: String): String {
        if (cipherText.isEmpty()) return ""

        runCatching {
            val decoded = Base64.getDecoder().decode(cipherText)
            val nonce = decoded.copyOfRange(0, GCM_NONCE_LENGTH)
            val cipherTextOnly = decoded.copyOfRange(GCM_NONCE_LENGTH, decoded.size)

            val cipher = Cipher.getInstance(AES_ALGORITHM)

            val gcmSpec = generateGCMParameter(nonce)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)

            return String(cipher.doFinal(cipherTextOnly), Charsets.UTF_8)
        }.getOrElse {
            logger.error("Unable to decrypt data")
            throw it
        }
    }
}