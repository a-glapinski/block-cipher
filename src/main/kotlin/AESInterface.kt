import javax.crypto.Cipher

interface AESInterface {
    val cipher: Cipher
    fun encrypt(input: String, key: String): String
    fun decrypt(input: String, key: String): String
}