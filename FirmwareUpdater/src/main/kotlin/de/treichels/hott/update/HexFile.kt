package de.treichels.hott.update

import de.treichels.hott.util.ByteOrder
import de.treichels.hott.util.readUShort
import java.io.File

private class Decoder {
    private var value = 1L

    private fun next(): Int {
        value = value * 214013L + 2531011L
        return ((value ushr 16) and 0x7fff).toInt()
    }

    private fun decode(i: Int) = i xor next() % 0xff
    internal fun decode(b: Byte) = decode(b.toInt()).toByte()
}

fun ByteArray.decode() = Decoder().let { d -> map { d.decode(it) }.toByteArray() }
fun File.decode(target: File) = target.writeBytes(readBytes().decode())

@ExperimentalUnsignedTypes
class Hex(val size: Int, val address: Int, val recordType: Int, val data: ByteArray) {
    companion object {
        internal fun parseLine(line: String): Hex {
            if (!line.startsWith(":")) throw IllegalArgumentException("line does not start with \":\"")

            // parse hex numbers into list of integers
            val dataSize = (line.length - 1) / 2
            val values = UByteArray(dataSize) {
                val s = line.substring(it * 2 + 1..it * 2 + 2)
                s.toUByte(16)
            }

            val size = values[0].toInt()
            val address = values.readUShort(1, ByteOrder.BigEndian).toInt()
            val recordType = values[3].toInt()
            val data = values.asByteArray().sliceArray(4 until 4 + size)
            val checksum = values.last()
            val calculated = (checksum.toInt() - values.sumBy { it.toInt() }).toUByte()

            if (checksum != calculated) throw IllegalArgumentException("checksum mismatch $checksum != $calculated")

            return Hex(size, address, recordType, data)
        }
    }

    private fun dumpData() = data.map { (it.toInt() and 0xff).toString(16) }.joinToString(separator = " ") {
        if (it.length == 1) "0$it" else it
    }

    override fun toString(): String {
        return "Hex(size=$size, address=0x${address.toString(16)}, recordType=$recordType, data=${dumpData()}})"
    }
}

@ExperimentalUnsignedTypes
class HexFile(private val lines: List<Hex>) : List<Hex> by lines {
    companion object {
        fun parse(file: File, decode: Boolean = true) = if (decode) parse(String(file.readBytes().decode())) else parse(file.readLines())
        fun parse(text: String) = parse(text.lines())
        fun parse(lines: List<String>) = HexFile(lines.filter { it.isNotBlank() }.map { Hex.parseLine(it) })
    }

    fun range(): Pair<Int, Int> {
        var min = Int.MAX_VALUE
        var max = 0
        var extAddr = 0

        forEach { hex ->
            when (hex.recordType) {
                0 -> {
                    // data record
                    val address = extAddr + hex.address
                    min = minOf(min, address)
                    max = maxOf(max, address + hex.size)
                }

                1 -> {
                    // end-of-file
                    return Pair(min, max)
                }

                4 -> {
                    // extended address record
                    extAddr = hex.data.readUShort(byteOrder = ByteOrder.BigEndian).toInt() shl 16
                }

                else -> throw IllegalArgumentException("invalid record type")
            }
        }

        throw java.lang.IllegalArgumentException("file does not end with an end-of-file record")
    }

    fun data(): ByteArray {
        val data = ByteArray(range().second)
        var extAddr = 0

        forEach { hex ->
            when (hex.recordType) {
                0 -> {
                    // data record
                    val address = extAddr + hex.address
                    System.arraycopy(hex.data, 0, data, address, hex.size)
                }

                1 -> {
                    // end-of-file
                    return data
                }

                4 -> {
                    // extended address record
                    extAddr = hex.data.readUShort(byteOrder = ByteOrder.BigEndian).toInt() shl 16
                }

                else -> throw IllegalArgumentException("invalid record type")
            }
        }

        throw java.lang.IllegalArgumentException("file does not end with an end-of-file record")
    }
}
