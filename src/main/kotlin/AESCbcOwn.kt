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
        val blocks = input.pkcs5Padding().chunked(16)

        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptedBytes = run {
            val iterator = blocks.iterator()
            generateSequence(
                cipher.doFinal(iv xor iterator.next())
            ) { prevResult ->
                if (iterator.hasNext()) cipher.doFinal(prevResult xor iterator.next())
                else null
            }
        }.toByteArray()

        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    override fun decrypt(input: String, key: String): String {
        val decodedText = Base64.getDecoder().decode(input)
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")

        val iv = key.take(16).toByteArray()
        val encryptedBlocks = decodedText.chunked(16)

        cipher.init(Cipher.DECRYPT_MODE, secretKey)

        val decryptedBytes = sequence {
            yield(cipher.doFinal(encryptedBlocks.first()) xor iv)

            for (i in 1 until encryptedBlocks.size) {
                yield(cipher.doFinal(encryptedBlocks[i]) xor encryptedBlocks[i - 1])
            }
        }.toByteArray()

        return decryptedBytes.pkcs5Trimming()
    }

    private infix fun ByteArray.xor(other: ByteArray): ByteArray =
        this.zip(other) { thisByte, otherByte -> thisByte xor otherByte }.toByteArray()

    private fun String.pkcs5Padding(blockSize: Int = 16): ByteArray {
        val paddingLength = blockSize - (this.length % blockSize)
        val padding = ByteArray(paddingLength) { paddingLength.toByte() }
        return this.toByteArray() + padding
    }

    private fun ByteArray.pkcs5Trimming(): String {
        val paddingLength = this.last().toInt()
        return String(this.take(this.size - paddingLength).toByteArray())
    }
}

private fun Sequence<ByteArray>.toByteArray() = this.flatMap { it.asSequence() }.toList().toByteArray()

private fun ByteArray.chunked(size: Int) = this.toList().chunked(size).map { it.toByteArray() }