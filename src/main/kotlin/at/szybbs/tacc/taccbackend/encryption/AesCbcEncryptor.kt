package at.szybbs.tacc.taccbackend.encryption

import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * A class for AES encryption and decryption using CBC mode with PKCS5 padding.
 *
 * @param password the password used to derive the encryption key.
 * @param salt the salt used to derive the encryption key.
 */
class AesCbcEncryptor (
    private val password : String,
    private val salt: String,
) {
    private val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    private val key = getKeyFromPasswordAndSalt(password, salt)

    /**
     * Generates a cryptographic key based on the provided password and salt using PBKDF2 with HMAC-SHA256.
     *
     * @param password the password used for key derivation.
     * @param salt the salt used for key derivation.
     * @return the generated secret key.
     * @throws NoSuchAlgorithmException if the algorithm PBKDF2WithHmacSHA256 is not available.
     * @throws InvalidKeySpecException if the provided key specification is invalid.
     */
    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun getKeyFromPasswordAndSalt(password: String, salt: String): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt.toByteArray(), 65536, 256)
        val secret: SecretKey = SecretKeySpec(
            factory.generateSecret(spec)
                .encoded, "AES"
        )
        return secret
    }

    /**
     * Generates a random 16-byte initialization vector (IV).
     *
     * @return the generated IV wrapped in an IvParameterSpec object.
     */
    private fun generateIv(): IvParameterSpec {
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        return IvParameterSpec(iv)
    }

    /**
     * Encrypts the given plaintext string using AES/CBC/PKCS5Padding.
     *
     * @param data the plaintext string to encrypt.
     * @return the encrypted data as a Base64-encoded string.
     *         Returns an empty string if the input is empty.
     * @throws IllegalArgumentException if the encryption process fails.
     */
    fun encrypt(data: String): String {
        if (data.isEmpty()) return ""

        val ivSpec = generateIv()

        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
        val cipherText = cipher.doFinal(data.toByteArray(Charsets.UTF_8))

        val ivAndCipher = ivSpec.iv + cipherText

        return Base64.getEncoder().encodeToString(ivAndCipher)
    }

    /**
     * Decrypts the given Base64-encoded ciphertext string using AES/CBC/PKCS5Padding.
     *
     * @param data the Base64-encoded ciphertext to decrypt.
     * @return the decrypted plaintext string.
     *         Returns an empty string if the input is empty.
     * @throws IllegalArgumentException if the decryption process fails.
     */
    fun decrypt(data: String): String {
        if (data.isEmpty()) return ""

        val ivAndCipher = Base64.getDecoder().decode(data)
        val iv = ivAndCipher.copyOfRange(0, 16)
        val cipherText = ivAndCipher.copyOfRange(16, ivAndCipher.size)
        val ivSpec = IvParameterSpec(iv)

        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)

        val attribute = cipher.doFinal(cipherText)
        return String(attribute, Charsets.UTF_8)
    }
}