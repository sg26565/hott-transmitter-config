import java.nio.charset.Charset
import java.util.zip.ZipFile

fun main(vararg args: String) {
    val file = "C:\\Users\\olive\\.java\\cache\\HoTT\\firmware\\mz32beta\\mz-32_r1010.zip"

    val zipFile = ZipFile(file, Charsets.US_ASCII)
    zipFile.stream().forEach {
        println(it)
    }
}