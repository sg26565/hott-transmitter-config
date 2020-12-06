fun main() {
    val regex = Regex("^([0-9]+)_?(.*)\\.(.*)$")

    listOf("10.bmp", "010.bmp", "10_bla.bmp", "010_bla.bmp").forEach {
        regex.matchEntire(it)?.groupValues?.forEachIndexed { i, s -> println("$it -> group $i: $s")}
    }
}
