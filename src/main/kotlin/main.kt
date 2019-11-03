fun main() {
    val key = "mvLBiZsiTbGwrfJB"
    val input = "abcdabcdabcdabcdabcdabcdabcdabcd".take(16)

    val s = AesCbcOwn.encrypt(input, key)
    println(s)
    val x = AesCbcOwn.decrypt(s, key)
    println(x)
}