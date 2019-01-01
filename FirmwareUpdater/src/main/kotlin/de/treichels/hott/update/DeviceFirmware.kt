package de.treichels.hott.update

import com.fazecast.jSerialComm.SerialPort
import de.treichels.hott.model.enums.ReceiverType
import de.treichels.hott.model.enums.Registered
import de.treichels.hott.util.Util
import javafx.concurrent.Task
import java.io.File
import java.io.IOException
import java.util.*

abstract class DeviceFirmware(val deviceType: Registered<*>, val packets: Array<ByteArray>) {
    companion object {
        @JvmStatic
        var messages = ResourceBundle.getBundle(DeviceFirmware::class.java.name)!!

        const val retryCount = 5
        private val knownIds = ReceiverType.values().map { it.id }.filterNot { it == 0 } + 0x05 + 0x0a
        internal fun List<Registered<*>>.forProductCode(productCode: Int) = find { it.productCode == productCode}

        fun load(fileName: String) = DeviceFirmware.load(File(fileName))
        fun load(file: File): DeviceFirmware = try {
            StandardDeviceFirmware.load(file)
        } catch (e1: IOException) {
            try {
                GyroReceiverFirmware.load(file)
            } catch (e2: IOException) {
                throw IOException("${e1.message}/${e2.message}", e2)
            }
        }

        fun detectDevice(task: Task<*>, port: SerialPort): Registered<*>? = port.use {
            it.setup()

            // wait for device boot
            val rc = it.waitForBoot(task, *knownIds.toIntArray())

            if (rc == 0x05 || rc == 0x0a) {
                // standard device
                StandardDeviceFirmware.updateMode(rc, it)
                Thread.sleep(100)
                val (productCode, _, _) = StandardDeviceFirmware.getInfo(it)
                deviceList.forProductCode(productCode)
            } else {
                // gyro/slowflyer receiver
                ReceiverType.forId(rc)
            }
        }
    }

    fun dump(): String {
        val result = StringBuffer()

        result.append(toString()).append('\n')
        result.append("ProductCode: ${deviceType.productCode}\n")
        result.append("PacketCount: ${packets.size}\n")
        result.append("PacketSize:  ${packets[0].size}\n")
        packets.forEachIndexed { index, bytes ->
            result.append("Packet $index\n")
            result.append(Util.dumpData(bytes))
        }

        return result.toString()
    }

    abstract fun updateDevice(task: FirmwareUpdateService.FirmwareUpdateTask, port: SerialPort)
}
