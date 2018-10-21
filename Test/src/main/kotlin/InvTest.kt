fun Byte.unsigned()  = this.toInt() and 0xff
fun Short.unsigned() = this.toInt() and 0xffff
fun Int.unsigned() = this.toLong() and 0xffffffff

fun Long.toSignedInt() = (this and 0xffffffff).toInt()
fun Int.toSignedShort() = (this and 0xffff).toShort()
fun Int.toSignedByte() = (this and 0xff).toByte()

fun Int.inv8() = this.inv() and 0xff

fun main(vararg ags: String) {
    for (i in 0..255) {
        println(String.format("%d %d (0x%02x) -> %d %d (0x%02x)", i, i.toSignedByte(), i.toSignedByte(),i.inv8(), i.inv8().toSignedByte(), i.inv8().toSignedByte()))
    }
}