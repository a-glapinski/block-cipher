import java.io.File
import kotlin.system.measureTimeMillis

const val KEY = "mvLBiZsiTbGwrfJB"
val files = listOf(
    File("10mb.txt"),
    File("100mb.txt"),
    File("200mb.txt")
)

fun main() {
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
    val modes = enumValues<AesWithIv.Mode>()

    encryptAndMeasureTime(text).also { (time, result) ->
        println("ECB - encrypt: $time ms | decrypt: ${measureDecryptionTime(result)} ms")
    }
    modes.forEach { mode ->
        encryptAndMeasureTime(text, mode).also { (time, result) ->
            println("$mode - encrypt: $time ms | decrypt: ${measureDecryptionTime(result, mode)} ms")
        }
    }
}

fun encryptAndMeasureTime(plainText: String, mode: AesWithIv.Mode? = null): Pair<Long, String> {
    val cipher = mode?.let { AesWithIv(it) } ?: AesEcb

    return measureTimeMillisWithResult { cipher.encrypt(plainText, KEY) }
}

fun measureDecryptionTime(encryptedText: String, mode: AesWithIv.Mode? = null): Long {
    val cipher = mode?.let { AesWithIv(it) } ?: AesEcb

    return measureTimeMillis { cipher.decrypt(encryptedText, KEY) }
}

inline fun <T> measureTimeMillisWithResult(block: () -> T): Pair<Long, T> {
    val start = System.currentTimeMillis()
    val result = block()
    val time = System.currentTimeMillis() - start
    return Pair(time, result)
}