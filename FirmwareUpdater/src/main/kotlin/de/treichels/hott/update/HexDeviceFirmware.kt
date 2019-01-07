package de.treichels.hott.update

import com.fazecast.jSerialComm.SerialPort
import de.treichels.hott.model.enums.ESCType
import de.treichels.hott.model.enums.ModuleType
import de.treichels.hott.model.enums.Registered
import de.treichels.hott.util.ByteOrder
import de.treichels.hott.util.readInt
import java.io.File
import java.io.IOException

class HexDeviceFirmware(deviceType: Registered<*>, val version: Int, packets: Array<ByteArray>) : DeviceFirmware(deviceType, packets) {
        companion object {
        fun load(file: File): HexDeviceFirmware {
            val hex = try {
                // first try encodes hex file
                HexFile.parse(file, true)
            } catch (e: IllegalArgumentException) {
                // then try unencodes hex file
                HexFile.parse(file, false)
            }

            val productCode = hex.data().readInt(0x2000, ByteOrder.LittleEndian)
            val deviceType: Registered<*>? = ESCType.forProductCode(productCode)
                    ?: ModuleType.forProductCode(productCode)
            val version = hex.data().readInt(0x2004)

            if (deviceType == null) throw IOException("unknown product code $productCode")
            println("found firmware for device $deviceType version $version")

            return HexDeviceFirmware(deviceType, version, arrayOf(hex.data()))
        }
    }

    override fun updateDevice(task: FirmwareUpdateService.FirmwareUpdateTask, port: SerialPort) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
