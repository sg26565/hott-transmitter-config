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
                val receiverType = ReceiverType.forId(id) ?: throw IOException(String.format(messages["unknownReceiver"], id))
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
        val baudrate = if (id >= 0xf0) 115200 else 19200

        // open serial port
        port.setComPortParameters(baudrate, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY)
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING + SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 1000)
        port.openPort()

        // wait for receiver
        task.print(messages["waitForReceiver"])
        task.print(String.format(messages["waitForReceiverID"],id))
        while (true) {
            try {
                val rc = port.read()
                print("$rc ")
                if (rc == id) break
            } catch (e: IOException) {
                if (task.isCancelled) return
            }
        }

        // write data
        val packetCount = packets.size
        packets.forEachIndexed { index, bytes ->
            task.progress(index + 1, packetCount)
            task.print(String.format(messages["writePacket"],index))

            // write block to receiver
            port.writeBytes(bytes)
            if (task.isCancelled) return

            // read response
            port.readBytes(response)
            if (task.isCancelled) return

            // read status
            val byte = port.read()

            if (!(response contentEquals bytes && byte == 0xaa)) throw IOException(messages["transmissionError"])
            if (task.isCancelled) return
        }

        // end transfer
        response[2] = 0xee.toByte()
        port.writeBytes(response)
        Thread.sleep(1000)
        task.print(messages["done"])
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
