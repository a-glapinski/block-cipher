import AES.Mode.CBC
import java.io.File
import kotlin.system.measureTimeMillis

const val INPUT = "ala ma kota"
const val KEY = "mvLBiZsiTbGwrfJB"

val files = listOf(
    File("10mb.txt"),
    File("100mb.txt"),
    File("200mb.txt")
)

fun main() {
    val encrypted = AESCbcOwn.encrypt(INPUT, KEY)
    val decrypted = AESCbcOwn.decrypt(encrypted, KEY)
    println(encrypted)
    println(decrypted)

    init()
}

fun init() {
    println("Initializing files...")
    val fileTexts = files.map { file ->
        file.name to file.bufferedReader().useLines { it.joinToString("") }
    }
    println("Done!\n")

    // Warm up
    buildString(100_000_000) {
        val alphabet = 'A'..'Z'
        repeat(100_000_000) { append(alphabet.random()) }
    }.let {
        encryptAndMeasureTime(it, KEY, CBC).also { (_, result) -> measureDecryptionTime(result, KEY, CBC) }
    }

    fileTexts.forEach { (name, text) ->
        println(name)
        printEncryptionTime(text)
        println()
    }
}

fun printEncryptionTime(text: String) {
    val modes = enumValues<AES.Mode>()

    modes.forEach { mode ->
        encryptAndMeasureTime(text, KEY, mode).also { (time, result) ->
            println("$mode - encrypt: $time ms | decrypt: ${measureDecryptionTime(result, KEY, mode)} ms")
        }
    }
}

fun encryptAndMeasureTime(plainText: String, key: String, mode: AES.Mode): Pair<Long, String> {
    val cipher = AES(mode)

    return measureTimeMillisWithResult { cipher.encrypt(plainText, key) }
}

fun measureDecryptionTime(encryptedText: String, key: String, mode: AES.Mode): Long {
    val cipher = AES(mode)

    return measureTimeMillis { cipher.decrypt(encryptedText, key) }
}

inline fun <R> measureTimeMillisWithResult(block: () -> R): Pair<Long, R> {
    val start = System.currentTimeMillis()
    val result = block()
    val time = System.currentTimeMillis() - start
    return Pair(time, result)
}