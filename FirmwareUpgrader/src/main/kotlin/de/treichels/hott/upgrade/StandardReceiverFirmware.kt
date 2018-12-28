package de.treichels.hott.upgrade

import com.fazecast.jSerialComm.SerialPort
import de.treichels.hott.model.enums.ReceiverType
import de.treichels.hott.util.readInt
import de.treichels.hott.util.readUnsignedInt
import de.treichels.hott.util.readUnsignedShort
import tornadofx.*
import java.io.File
import java.io.IOException
import java.util.concurrent.CountDownLatch

class StandardReceiverFirmware(receiverType: ReceiverType, val version: Int, packets: Array<ByteArray>) : ReceiverFirmware(receiverType, packets) {
    companion object {
        fun load(file: File): StandardReceiverFirmware {
            file.inputStream().use { stream ->
                // read file header
                val flag1 = stream.readUnsignedInt()
                if (flag1 != 0x23456789L && flag1 != 0x12345678L) throw IOException(messages["invalidFileHeader"])

                val flag2 = stream.readUnsignedInt()
                if (0xffffffffL - flag2 != flag1) throw IOException(messages["invalidFileHeader"])

                //val deviceId = stream.readInt()
                //val processId = stream.readInt()
                stream.skip(8)

                val productCode = stream.readInt()
                val receiverType = ReceiverType.forProductCode(productCode)
                        ?: throw IOException(String.format(messages["unknownReceiver"], productCode))
                val version = stream.readInt()
                val packetCount = stream.readInt()

                stream.skip(36)

                val packets = Array(packetCount) {
                    val flag = stream.read()
                    if (flag != 0) throw IOException(messages["invalidBlock"])

                    val seq1 = stream.readUnsignedShort()
                    val seq2 = stream.readUnsignedShort()
                    if (seq1 != 0xffff - seq2) throw IOException(messages["invalidBlock"])

                    val size = stream.readUnsignedShort()
                    stream.skip(9)

                    val data = ByteArray(size)
                    stream.read(data)

                    data
                }

                return StandardReceiverFirmware(receiverType, version, packets)
            }
        }
    }

    override fun upgradeReceiver(task: FirmwareUpgradeService.FirmwareUpgradeTask, port: SerialPort) {
        var rc: Int

        port.setComPortParameters(19200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY)
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING + SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0)
        port.openPort()

        task.print(messages["waitForReceiver"])
        while (true) {
            try {
                rc = port.read()
                if (rc == 0x05 || rc == 0x0a) break
            } catch (e: IOException) {
                if (task.isCancelled) return
            }
        }

        if (rc == 0x05) rc = 0x09
        port.write(rc)
        port.expect(rc)

        Thread.sleep(100)
        port.write(0x09)
        port.expect(0x09)
        port.readBytes(ByteArray(8))
        //val flag1 = port.readInt()
        //val flag2 = port.readInt()
        //println("Flag1=0x${flag1.toString(16)} ($flag1), Flag2=0x${flag2.toString(16)} ($flag2)")

        Thread.sleep(100)
        port.write(0x10)
        port.expect(0x10)
        val productCode = port.readInt()
        val appVersion = port.readInt()
        val bootVersion = port.readInt()

        if (productCode != receiverType.productCode) throw IOException(String.format(messages["invalidReceiverType"], productCode, receiverType.productCode))

        val latch = CountDownLatch(1)
        var doUpdate = false

        runLater {
            confirm(messages["currentVersion"], String.format(messages["versionInfo"], productCode, appVersion/1000.0, bootVersion/1000.0)) {
                doUpdate = true
            }
            latch.countDown()
        }

        latch.await()

        if (doUpdate) {
            val packetCount = packets.size
            packets.forEachIndexed { index, data ->
                task.progress(index + 1, packetCount)
                task.print(String.format(messages["writePacket"], index))

                val response = ByteArray(data.size)
                port.writeBytes(data)
                port.readBytes(response)

                if (!response.contentEquals(data)) throw IOException(messages["transmissionError"])

                port.expect(0x01)
            }

            task.print(messages["done"])
        } else task.cancel()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StandardReceiverFirmware

        if (receiverType != other.receiverType) return false
        if (version != other.version) return false
        if (!packets.contentDeepEquals(other.packets)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = receiverType.hashCode()
        result = 31 * result + version
        result = 31 * result + packets.contentDeepHashCode()
        return result
    }

    override fun toString(): String {
        return "StandardReceiverFirmware($receiverType, v${version / 1000.0})"
    }
}
