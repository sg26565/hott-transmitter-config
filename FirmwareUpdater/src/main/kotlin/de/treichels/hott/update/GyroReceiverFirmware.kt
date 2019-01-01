package de.treichels.hott.update

import com.fazecast.jSerialComm.SerialPort
import de.treichels.hott.model.enums.ReceiverType
import tornadofx.*
import java.io.File
import java.io.IOException

class GyroReceiverFirmware(receiverType: ReceiverType, packets: Array<ByteArray>) : DeviceFirmware(receiverType, packets) {
    companion object {
        const val blockSize = 0x406

        fun load(file: File): GyroReceiverFirmware {
            file.inputStream().use { stream ->
                val size = file.length()
                val headerSize = 8
                val blockCount = (size.toInt() - headerSize) / blockSize
                val header = ByteArray(headerSize).apply { stream.read(this) }
                val id = header[0].toInt() and 0xFF
                val receiverType = ReceiverType.forId(id)
                        ?: throw IOException(String.format(messages["unknownDevice"], id))
                val packets = Array(blockCount) {
                    ByteArray(blockSize).apply {
                        stream.read(this)
                        if (this[0] + (this[1].toInt() shl 8) != blockSize) throw IOException(messages["invalidBlock"])
                    }
                }

                return GyroReceiverFirmware(receiverType, packets)
            }
        }
    }

    override fun updateDevice(task: FirmwareUpdateService.FirmwareUpdateTask, port: SerialPort) {
        val response = ByteArray(GyroReceiverFirmware.blockSize)
        val id = (deviceType as ReceiverType).id

        // open serial port
        port.setup(if (id >= 0xf0) 115200 else 19200)
        port.use {
            // wait for device
            task.print(messages["waitForDevice"])
            task.print(String.format(messages["waitForDeviceID"], deviceType))
            while (true) {
                try {
                    val rc = it.read()
                    print("$rc ")
                    if (rc == id) break
                } catch (e: IOException) {
                    if (task.isCancelled) {
                        task.print(messages["cancelled"])
                        return@use
                    }
                }
            }

            try {
                // write data
                val packetCount = packets.size
                packets.forEachIndexed { index, data ->
                    var retries = 0

                    while (true) {
                        try {
                            task.progress(index + 1, packetCount)
                            task.print(String.format(messages["writePacket"], index+1, packetCount))

                            if (task.isCancelled) {
                                task.print(messages["cancelled"])
                                return@use
                            }

                            // write block to device
                            it.writeBytes(data)
                            it.readBytes(response)
                            if (!response.contentEquals(data)) throw IOException(messages["transmissionError"])
                            it.expect(0xaa)
                            break
                        } catch (e: IOException) {
                            // retry on error up to retryCount times
                            if (retries++ >= retryCount) throw e
                        }
                    }
                }

                // end transfer
                response[2] = 0xee.toByte()
                it.writeBytes(response)
                Thread.sleep(1000)
                task.print(messages["done"])
            } catch (e: IOException) {
                task.print(messages["transmissionError"])
                throw e
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GyroReceiverFirmware

        if (deviceType != other.deviceType) return false
        if (!packets.contentDeepEquals(other.packets)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = deviceType.hashCode()
        result = 31 * result + packets.contentDeepHashCode()
        return result
    }

    override fun toString(): String {
        return "GyroReceiverFirmware($deviceType)"
    }
}
