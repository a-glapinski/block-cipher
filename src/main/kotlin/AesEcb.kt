import java.security.InvalidKeyException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AesEcb : Aes {
    override val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")

    override fun encrypt(input: String, key: String): String {
        val encrypted: ByteArray = try {
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")

            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            cipher.doFinal(input.toByteArray())
        } catch (e: InvalidKeyException) {
            throw e
        }

        return Base64.getEncoder().run { encodeToString(encrypted) }
    }

    override fun decrypt(input: String, key: String): String {
        val output: ByteArray = try {
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")

            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            Base64.getDecoder().run { cipher.doFinal(decode(input)) }
        } catch (e: InvalidKeyException) {
            throw e
        }
        return String(output)
    }

    fun encryptToByteArray(bytes: ByteArray, key: String) = encrypt(String(bytes), key).toByteArray()
    fun decryptToByteArray(bytes: ByteArray, key: String) = decrypt(String(bytes), key).toByteArray()
}