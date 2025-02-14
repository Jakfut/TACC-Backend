package at.szybbs.tacc.taccbackend.encryption

interface StringEncryptor {
    fun encrypt(plainText: String): String
    fun decrypt(cipherText: String): String
}