fun Int.inv8() = this.inv().toUByte()

fun main() {
    for (i in 0..255) {
        println(String.format("%d %d (0x%02x) -> %d %d (0x%02x)", i, i.toByte(), i.toByte(),i.inv8(), i.inv8().toByte(), i.inv8().toByte()))
    }
}
