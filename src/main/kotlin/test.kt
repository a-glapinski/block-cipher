const val INPUT = "abcdabcdabcdabcdabcdabcdabcdabcd"

fun main() {
    ecb()
    cbc()
    ofb()
    cfb()
    ctr()
}

fun ecb() {
    println("ECB")
    val encrypted = AesEcb.encrypt(INPUT, KEY)
    val decrypted = AesEcb.decrypt(encrypted, KEY)

    println(encrypted)
    println(decrypted)
}

fun cbc() {
    println("\nCBC")
    val aesCbc = AesWithIv(AesWithIv.Mode.CBC)
    val encrypted = aesCbc.encrypt(INPUT, KEY)
    val decrypted = aesCbc.decrypt(encrypted, KEY)

    println(encrypted)
    println(decrypted)
}

fun ofb() {
    println("\nOFB")
    val aesOfb = AesWithIv(AesWithIv.Mode.OFB)
    val encrypted = aesOfb.encrypt(INPUT, KEY)
    val decrypted = aesOfb.decrypt(encrypted, KEY)

    println(encrypted)
    println(decrypted)
}

fun cfb() {
    println("\nCFB")
    val aesCfb = AesWithIv(AesWithIv.Mode.CFB)

    val encrypted = aesCfb.encrypt(INPUT, KEY)
    val decrypted = aesCfb.decrypt(encrypted, KEY)

    println(encrypted)
    println(decrypted)
}

fun ctr() {
    println("\nCTR")
    val aesCtr = AesWithIv(AesWithIv.Mode.CTR)

    val encrypted = aesCtr.encrypt(INPUT, KEY)
    val decrypted = aesCtr.decrypt(encrypted, KEY)

    println(encrypted)
    println(decrypted)
}