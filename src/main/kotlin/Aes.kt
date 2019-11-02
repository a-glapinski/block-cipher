import javax.crypto.Cipher

interface Aes {
    val cipher: Cipher
    fun encrypt(input: String, key: String): String
    fun decrypt(input: String, key: String): String
}