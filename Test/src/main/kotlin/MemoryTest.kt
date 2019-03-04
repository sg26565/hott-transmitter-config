@file:Suppress("unused", "MemberVisibilityCanBePrivate")

import de.treichels.hott.util.Util
import de.treichels.hott.util.shr
import java.io.File
import java.io.IOException

const val COMMAND_NACK = 0x00
const val COMMAND_ACK = 0x01
const val COMMAND_READ_PM = 0x02
const val COMMAND_WRITE_PM = 0x03
const val COMMAND_READ_EE = 0x04
const val COMMAND_WRITE_EE = 0x05
const val COMMAND_READ_CM = 0x06
const val COMMAND_WRITE_CM = 0x07
const val COMMAND_RESET = 0x08
const val COMMAND_READ_ID = 0x09
const val COMMAND_START = 0x0A
const val COMMAND_START_TELEMETRY = 0x0B
const val COMMAND_READ_VER = 0x10  // 2009-08-11
const val COMMAND_READ_ID_ADDR = 0x11  // 2009-08-21 : Address Info

const val COMMAND_START_BOOT5 = 0x05
const val COMMAND_START_BOOT5_RESP = 0x09

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

enum class Family(val id: Int) {
    dsPIC30F(1), dsPIC33F(2), PIC24H(3), PIC24F(4), DSPIC33EP(5), SAMSUNG(100), S3F828B(101),
    S3F84ZB(102), S3F84YB(103), ATMEAL(200), ATmega128(201), CC2510(300)
}

enum class MicomType(val id: Int) {
    Micom_DeviceID_Samsung(1), Micom_DeviceID_ATmega(2), Micom_DeviceID_TI(3)
}

data class Device(val pName: String, val id: Int, val processId: Int, val family: Family)
data class Version(val productCode: Int, val appVer: Int, val bootVer: Int, val sProductCode: String, val sAppVer: String, val sBootVer: String)

class MemoryPic(val flashType: FlashType, val family: Family, baseAddress: Int, val rowNumber: Int) {
    var empty = true
    val buffer: UByteArray
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
        buffer = UByteArray(buffSize)
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
                    buffer[0 + count * 3] = (addrData[count * 2] shr 8).toUByte()
                    buffer[1 + count * 3] = (addrData[count * 2]).toUByte()
                    buffer[2 + count * 3] = (addrData[count * 2 + 1] shr 8).toUByte()
                }
            }
            FlashType.EEProm -> {
                for (count in 0 until rowSize) {
                    buffer[0 + count * 2] = (addrData[count * 2] shr 8).toUByte()
                    buffer[1 + count * 2] = (addrData[count * 2]).toUByte()
                }
            }
            FlashType.FuseBits -> {
                buffer[0] = (addrData[0] shr 8).toUByte()
                buffer[1] = (addrData[0]).toUByte()
                buffer[2] = (addrData[1] shr 8).toUByte()
            }
        }
    }

    override fun toString(): String {
        return "MemoryPic(flashType=$flashType, family=$family, rowNumber=$rowNumber, empty=$empty, rowSize=0x${rowSize.toString(16)}, address=0x${address.toString(16)})"
    }

    fun dump() {
        dataFormat()
        println(this)
        println(Util.dumpData(addrData.toShortArray(), address))
        println(Util.dumpData(buffer.toByteArray(), address))
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
fun File.decode() = String(readBytes().decode())

fun main() {
    val file = File("C:\\Users\\olive\\.java\\cache\\HoTT\\firmware\\bl_control_35\\AIR3G_6S_ESC_Telemetry_2a011.bin")
    val hexFile = file.decode()

    val programmer = ProgrammerPIC(Family.dsPIC33F)
    programmer.hexFileParse(hexFile)

    programmer.memory.filterNot { it.empty }.forEach {
        it.dump()
    }
}

