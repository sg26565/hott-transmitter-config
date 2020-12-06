fun main() {
    read( "foo|123|path.exe")
    read( "foo|path.exe")
}

fun read(line: String) {
    try {
        val (hash, size, path) = line.split('|')
        println("$hash|${size.toLong()}|$path")
    } catch (e:Exception) {
        println ("malformed: $e")
    }
}
