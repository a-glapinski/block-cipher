import AES.Mode.ECB
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

    fileTexts.forEach { (name, text) ->
        println(name)
        printEncryptionTime(text)
        println()
    }
}

fun printEncryptionTime(text: String) {
    val modes = enumValues<AES.Mode>()

    modes.forEach { mode ->
        encryptAndMeasureTime(text, mode).also { (time, result) ->
            println("$mode - encrypt: $time ms | decrypt: ${measureDecryptionTime(result, mode)} ms")
        }
    }
}

fun encryptAndMeasureTime(plainText: String, mode: AES.Mode = ECB): Pair<Long, String> {
    val cipher = AES(mode)

    return measureTimeMillisWithResult { cipher.encrypt(plainText, KEY) }
}

fun measureDecryptionTime(encryptedText: String, mode: AES.Mode = ECB): Long {
    val cipher = AES(mode)

    return measureTimeMillis { cipher.decrypt(encryptedText, KEY) }
}

inline fun <R> measureTimeMillisWithResult(block: () -> R): Pair<Long, R> {
    val start = System.currentTimeMillis()
    val result = block()
    val time = System.currentTimeMillis() - start
    return Pair(time, result)
}