import kotlin.experimental.xor

/**
 * Own implementation based on ECB mode
 */
object AesCbcOwn {
    fun encrypt(plainText: String, key: String): String {
        val iv = key.take(16).toByteArray()

        val blocks = plainText.chunked(16).map { it.toByteArray() }
        val blocksIterator = blocks.iterator()

        val encryptedBytes = generateSequence(
            AesEcb.encryptToByteArray(iv xor blocksIterator.next(), key)
        ) {
            try {
                AesEcb.encryptToByteArray(it xor blocksIterator.next(), key)
            } catch (e: NoSuchElementException) {
                null
            }
        }

        return encryptedBytes.joinToString("") { String(it) }
    }

    fun decrypt(encryptedText: String, key: String): String {
        val iv = key.take(16).toByteArray()

        val encryptedBlocks = encryptedText.chunked(22).map { it.toByteArray() }
        val blocksIterator = encryptedBlocks.iterator()

        val decryptedBytes = generateSequence(
            AesEcb.decryptToByteArray(blocksIterator.next(), key) xor iv
        ) {
            try {
                AesEcb.decryptToByteArray(blocksIterator.next(), key) xor it
            } catch (e: NoSuchElementException) {
                null
            }
        }

        return decryptedBytes.joinToString("") { String(it) }
    }

    private infix fun ByteArray.xor(other: ByteArray) =
        this.zip(other) { thisByte, otherByte -> thisByte xor otherByte }.toByteArray()
}