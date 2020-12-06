@file:Suppress("unused", "MemberVisibilityCanBePrivate")

import de.treichels.hott.decoder.internal.firmware.Family
import de.treichels.hott.decoder.internal.firmware.HexDeviceFirmware.Companion.writePM
import de.treichels.hott.decoder.internal.firmware.HexFile
import de.treichels.hott.decoder.internal.firmware.decode
import de.treichels.hott.util.Util
import de.treichels.hott.util.shr
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile


const val BUFFER_SIZE = 4096
const val READ_BUFFER_TIMEOUT = 500

const val PM30F_ROW_SIZE = 32
const val PM33F_ROW_SIZE = 64 * 8    // 512WORD
const val EE30F_ROW_SIZE = 16

const val PM_SIZE = 1536 /* Max: 144KB/3/32=1536 PM rows for 30F. */
const val EE_SIZE = 128 /* 4KB/2/16=128 EE rows */
const val CM_SIZE = 8

enum class FlashType {
    Program, EEProm, FuseBits
}

// data class Version(val productCode: Int, val appVer: Int, val bootVer: Int, val sProductCode: String, val sAppVer: String, val sBootVer: String)

class MemoryPic(val flashType: FlashType, val family: Family, baseAddress: Int, val rowNumber: Int) {
    var empty = true
    val buffer: ByteArray
    val addrData = UShortArray(PM33F_ROW_SIZE * 2) { 0xffffu }
    val rowSize: Int
    val address: Int
    val maxAddress: Int

    init {
        val buffSize: Int

        when (flashType) {
            FlashType.Program -> {
                rowSize = if (family == Family.dsPIC30F) PM30F_ROW_SIZE else PM33F_ROW_SIZE
                buffSize = rowSize * 3
                address = baseAddress + rowNumber * rowSize * 2
            }
            FlashType.EEProm -> {
                rowSize = EE30F_ROW_SIZE
                buffSize = rowSize * 2
                address = baseAddress + rowNumber * rowSize * 2
            }
            FlashType.FuseBits -> {
                rowSize = EE30F_ROW_SIZE
                buffSize = 3
                address = baseAddress + rowNumber * 2
            }
        }

        maxAddress = if (flashType == FlashType.FuseBits) address + 2 else address + rowSize * 2
        buffer = ByteArray(buffSize)
    }

    fun dataRead(address: Int): Pair<Boolean, UShortArray?> {
        return if (empty || address < this.address || address >= maxAddress)
            Pair(false, null)
        else
            Pair(true, addrData.copyOfRange(address - this.address, addrData.size - 1))
    }

    fun dataInsert(address: Int, data: String): Boolean {
        if (address < this.address || address >= maxAddress) return false

        empty = false
        addrData[address - this.address] = data.substring(0..3).toUShort(16)
        return true
    }

    fun dataFormat() {
        if (empty) return

        when (flashType) {
            FlashType.Program -> {
                for (count in 0 until rowSize) {
                    buffer[0 + count * 3] = (addrData[count * 2] shr 8).toByte()
                    buffer[1 + count * 3] = (addrData[count * 2]).toByte()
                    buffer[2 + count * 3] = (addrData[count * 2 + 1] shr 8).toByte()
                }
            }
            FlashType.EEProm -> {
                for (count in 0 until rowSize) {
                    buffer[0 + count * 2] = (addrData[count * 2] shr 8).toByte()
                    buffer[1 + count * 2] = (addrData[count * 2]).toByte()
                }
            }
            FlashType.FuseBits -> {
                buffer[0] = (addrData[0] shr 8).toByte()
                buffer[1] = (addrData[0]).toByte()
                buffer[2] = (addrData[1] shr 8).toByte()
            }
        }
    }

    override fun toString(): String {
        return "MemoryPic(flashType=$flashType, family=$family, rowNumber=$rowNumber, empty=$empty, rowSize=0x${
            rowSize.toString(
                16
            )
        }, address=0x${address.toString(16)})"
    }

    fun dump() {
        dataFormat()
        println(this)
        println(Util.dumpData(addrData.toShortArray(), address))
        println(Util.dumpData(writePM(address, buffer)))
    }
}

class ProgrammerPIC(val family: Family) {
    val memory = Array(PM_SIZE + EE_SIZE + CM_SIZE) {
        when {
            it < PM_SIZE -> MemoryPic(FlashType.Program, family, 0x000000, it)
            it < PM_SIZE + EE_SIZE -> MemoryPic(FlashType.EEProm, family, 0x7ff000, it - PM_SIZE)
            else -> MemoryPic(FlashType.FuseBits, family, 0xF80000, it - PM_SIZE - EE_SIZE)
        }
    }

    var majorAddress = 0

    fun hexFileParse(hexFile: String) {
        var extAddr = 0

        hexFile.lines().forEach { line ->
            if (!line.startsWith(":")) throw IOException("line does not start with \":\"")

            val byteCount = line.slice(1..2).toInt(16)
            val baseAddress = line.slice(3..6).toInt(16)
            val recordType = line.slice(7..8).toInt(16)

            when (recordType) {
                0x00 -> {
                    var address = (baseAddress + extAddr) / 2

                    for (charCount in 0 until byteCount * 2 step 4) {
                        var inserted = false

                        for (row in 0 until PM_SIZE + EE_SIZE + CM_SIZE) {
                            val mem = memory[row]
                            inserted = mem.dataInsert(address, line.substring(9 + charCount))
                            if (inserted) {
                                if (mem.flashType == FlashType.Program) {
                                    if (majorAddress < address) {
                                        majorAddress = address
                                    }
                                }

                                break
                            }
                        }

                        if (!inserted) {
                            println("Hex file 0x${address.toString(16)} out of range")
                        }
                        address++
                    }
                }

                0x01 -> {
                    // end of file record
                    return
                }

                0x04 -> {
                    // extended address record
                    extAddr = line.slice(9..12).toInt(16) shl 16
                }
            }
        }

        throw IOException("file does not end with an end-of-file record")
    }
}

fun File.withExtension(newExtension: String) = File(parentFile, "$nameWithoutExtension.$newExtension")

fun main() {
    val binFile = File("C:\\Users\\olive\\.java\\cache\\HoTT\\firmware\\bl_control_18\\33718_AIR3G_4S18A_1a30.bin")
    val hex = String(binFile.decode())

    // write hex file
    binFile.withExtension("hex").writeText(hex)

    // use my implementation to write binary and reduced binary files
    HexFile.parse(binFile, true).chunks(0x800).forEach { address, data ->
        RandomAccessFile(binFile.withExtension("dat"), "rw").apply {
            seek(address.toLong())
            write(data)
        }

        RandomAccessFile(binFile.withExtension("dat1"), "rw").apply {
            val reduced = data.filterIndexed { index, _ -> index % 4 != 3 }.toByteArray()
            println(Util.dumpData(writePM(address / 2, reduced), address / 2))

            seek(address.toLong())
            write(reduced)
        }
    }

    // use Graupner's implementation to write binary and reduced binary files
    val programmer = ProgrammerPIC(Family.dsPIC33F)
    programmer.hexFileParse(hex)

    programmer.memory.filterNot { it.empty }.forEach {
        it.dataFormat()
        //println(Util.dumpData(writePM(null, it.address, it.buffer), it.address))


        RandomAccessFile(binFile.withExtension("dat2"), "rw").apply {
            seek(it.address.toLong() * 2)
            it.addrData.forEach { s ->
                write(s.toInt() ushr 8)
                write(s.toInt() and 0xff)
            }
        }

        RandomAccessFile(binFile.withExtension("dat3"), "rw").apply {
            seek(it.address.toLong() * 2)
            write(it.buffer)
        }
    }
}

