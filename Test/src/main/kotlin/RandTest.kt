import de.treichels.hott.update.HexFile
import de.treichels.hott.update.decode
import de.treichels.hott.util.ByteOrder
import de.treichels.hott.util.Util
import de.treichels.hott.util.readUShort
import java.io.File

@ExperimentalUnsignedTypes
fun main(vararg args: String) {
    val rootDir = File("C:/Users/olive/.java/cache/HoTT/firmware")

    rootDir.listFiles().filter { it.isDirectory }.forEach { dir ->
        dir.listFiles().filter { it.isFile && it.name.endsWith(".bin") }.forEach { binFile ->
            val base = binFile.name.substringBeforeLast(".")
            val hexFile = File(dir, "$base.hex")
            val dumpFile = File(dir, "$base.txt")
            val datFile = File(dir, "$base.dat")

            dumpFile.delete()
            binFile.decode(hexFile)

            try {
                val hex = HexFile.parse(hexFile, false)
                datFile.writeBytes(hex.data())
            } catch (e: IllegalArgumentException) {
                hexFile.delete()
            }
        }
    }
}
