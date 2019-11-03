import java.security.InvalidKeyException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AesWithIv(mode: Mode) : Aes {
    enum class Mode {
        CBC, OFB, CFB, CTR
    }

    override val cipher: Cipher = Cipher.getInstance("AES/$mode/PKCS5Padding")

    override fun encrypt(input: String, key: String): String {
        val encrypted: ByteArray = try {
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")
            val iv = IvParameterSpec(key.take(16).toByteArray())

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv)
            cipher.doFinal(input.toByteArray())
        } catch (e: InvalidKeyException) {
            throw e
        }

        return Base64.getEncoder().run { encodeToString(encrypted) }
    }

    override fun decrypt(input: String, key: String): String {
        val output: ByteArray = try {
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")
            val iv = IvParameterSpec(key.take(16).toByteArray())

            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv)
            Base64.getDecoder().run { cipher.doFinal(decode(input)) }
        } catch (e: InvalidKeyException) {
            throw e
        }

        return String(output)
    }

}