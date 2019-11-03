fun main() {
    val key = "mvLBiZsiTbGwrfJB"
    val input = "abcdabcdabcdabcdabcdabcdabcdabcd".take(16)

    val s = AesCbcOwn.encrypt(input, key)
    println(s)
    val x = AesCbcOwn.decrypt(s, key)
    println(x)
//
//    val s2 = AesEcb.encrypt(input, key)
//    val x2 = AesEcb.decrypt(s2, key)
//    println(s2)
//    println(x2)
}