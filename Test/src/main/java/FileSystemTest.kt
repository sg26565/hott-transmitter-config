import java.io.File
import java.nio.file.FileSystems

fun main(vararg args: String) {
    println("File.listRoots()")
    File.listRoots().forEach { println("\t$it") }

    val fs = FileSystems.getDefault()

    println("FileSystem.rootDirectories")
    fs.rootDirectories.forEach { println("\t$it") }

    println("FileSystem.")
    fs.fileStores.forEach { println("\t$it") }
}