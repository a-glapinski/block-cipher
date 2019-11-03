import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.xor

/**
 * Own implementation based on ECB mode
 */
object AESCbcOwn : AESInterface {
    override val cipher: Cipher = Cipher.getInstance("AES/ECB/NoPadding")

    override fun encrypt(input: String, key: String): String {
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")

        val iv = key.take(16).toByteArray()
        val blocks = input.chunked(16).map { it.toByteArray() }

        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptedBytes = with(blocks.iterator()) {
            generateSequence(
                cipher.doFinal(iv xor next())
            ) { prevResult ->
                try {
                    cipher.doFinal(prevResult xor next())
                } catch (e: NoSuchElementException) {
                    null
                }
            }
        }.toByteArray()

        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    override fun decrypt(input: String, key: String): String {
        val decodedText = Base64.getDecoder().decode(input)
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")

        val iv = key.take(16).toByteArray()
        val encryptedBlocks = decodedText.toList().chunked(16).map { it.toByteArray() }

        cipher.init(Cipher.DECRYPT_MODE, secretKey)

        val decryptedBytes = sequence {
            yield(cipher.doFinal(encryptedBlocks.first()) xor iv)

            for (i in 1 until encryptedBlocks.size) {
                yield(cipher.doFinal(encryptedBlocks[i]) xor encryptedBlocks[i - 1])
            }
        }.toByteArray()

        return String(decryptedBytes)
    }

    private infix fun ByteArray.xor(other: ByteArray): ByteArray =
        this.zip(other) { thisByte, otherByte -> thisByte xor otherByte }.toByteArray()
}

private fun Sequence<ByteArray>.toByteArray() = this.flatMap { it.asSequence() }.toList().toByteArray()