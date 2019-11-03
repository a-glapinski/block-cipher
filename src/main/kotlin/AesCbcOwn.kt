import java.util.*
import kotlin.NoSuchElementException
import kotlin.experimental.xor

/**
 * Own implementation based on ECB mode
 */
object AesCbcOwn {
    fun encrypt(plainText: String, key: String): String {
        val iv = key.take(16).toByteArray()
        val blocks = plainText.chunked(16).map { it.toByteArray() }

        val encryptedBytes = with(blocks.iterator()) {
            generateSequence(
                AesEcb.encryptToByteArray(iv xor next(), key)
            ) {
                try {
                    AesEcb.encryptToByteArray(it xor next(), key)
                } catch (e: NoSuchElementException) {
                    null
                }
            }
        }

        return encryptedBytes.joinToString("") { String(it) }
    }

    fun decrypt(encryptedText: String, key: String): String {
        val iv = key.take(16).toByteArray()
        val encryptedBlocks = encryptedText.chunked(16).map { it.toByteArray() }

        val decryptedBytes = with(encryptedBlocks.iterator()) {
            generateSequence(
                AesEcb.decryptToByteArray(next(), key) xor iv
            ) {
                try {
                    AesEcb.decryptToByteArray(next(), key) xor it
                } catch (e: NoSuchElementException) {
                    null
                }
            }
        }

        return decryptedBytes.joinToString("") { String(it) }
    }

    private infix fun ByteArray.xor(other: ByteArray) =
        this.zip(other) { thisByte, otherByte -> thisByte xor otherByte }.toByteArray()
}