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
    val aesEcb = AES(AES.Mode.ECB)
    val encrypted = aesEcb.encrypt(INPUT, KEY)
    val decrypted = aesEcb.decrypt(encrypted, KEY)

    println(encrypted)
    println(decrypted)
}

fun cbc() {
    println("\nCBC")
    val aesCbc = AES(AES.Mode.CBC)
    val encrypted = aesCbc.encrypt(INPUT, KEY)
    val decrypted = aesCbc.decrypt(encrypted, KEY)

    println(encrypted)
    println(decrypted)
}

fun ofb() {
    println("\nOFB")
    val aesOfb = AES(AES.Mode.OFB)
    val encrypted = aesOfb.encrypt(INPUT, KEY)
    val decrypted = aesOfb.decrypt(encrypted, KEY)

    println(encrypted)
    println(decrypted)
}

fun cfb() {
    println("\nCFB")
    val aesCfb = AES(AES.Mode.CFB)

    val encrypted = aesCfb.encrypt(INPUT, KEY)
    val decrypted = aesCfb.decrypt(encrypted, KEY)

    println(encrypted)
    println(decrypted)
}

fun ctr() {
    println("\nCTR")
    val aesCtr = AES(AES.Mode.CTR)

    val encrypted = aesCtr.encrypt(INPUT, KEY)
    val decrypted = aesCtr.decrypt(encrypted, KEY)

    println(encrypted)
    println(decrypted)
}