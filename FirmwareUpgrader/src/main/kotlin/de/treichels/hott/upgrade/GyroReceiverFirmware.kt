package de.treichels.hott.upgrade

import com.fazecast.jSerialComm.SerialPort
import de.treichels.hott.model.enums.ReceiverType
import tornadofx.*
import java.io.File
import java.io.IOException

class GyroReceiverFirmware(receiverType: ReceiverType, packets: Array<ByteArray>) : ReceiverFirmware(receiverType, packets) {
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
                        ?: throw IOException(String.format(messages["unknownReceiver"], id))
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

    override fun upgradeReceiver(task: FirmwareUpgradeService.FirmwareUpgradeTask, port: SerialPort) {
        val response = ByteArray(GyroReceiverFirmware.blockSize)
        val id = receiverType.id

        // open serial port
        port.setup(if (id >= 0xf0) 115200 else 19200)
        port.use {
            // wait for receiver
            task.print(messages["waitForReceiver"])
            task.print(String.format(messages["waitForReceiverID"], receiverType))
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
                packets.forEachIndexed { index, bytes ->
                    task.progress(index + 1, packetCount)
                    task.print(String.format(messages["writePacket"], index))

                    if (task.isCancelled) {
                        task.print(messages["cancelled"])
                        return@use
                    }

                    // write block to receiver
                    it.writeBytes(bytes)
                    it.readBytes(response)
                    if (!response.contentEquals(bytes)) throw IOException(messages["transmissionError"]) // TODO implement re-try
                    it.expect(0xaa)
                }

                // end transfer
                response[2] = 0xee.toByte()
                it.writeBytes(response)
                Thread.sleep(1000)
                task.print(messages["done"])
            } catch(e:IOException) {
                task.print(messages["transmissionError"])
                throw e
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GyroReceiverFirmware

        if (receiverType != other.receiverType) return false
        if (!packets.contentDeepEquals(other.packets)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = receiverType.hashCode()
        result = 31 * result + packets.contentDeepHashCode()
        return result
    }

    override fun toString(): String {
        return "GyroReceiverFirmware($receiverType)"
    }
}
