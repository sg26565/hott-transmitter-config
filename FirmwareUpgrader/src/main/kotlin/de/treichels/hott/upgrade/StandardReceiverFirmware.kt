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

        internal fun upgradeMode(rc: Int, port: SerialPort) {
            // switch to boot mode
            val expected = if (rc == 0x05) 0x09 else rc
            port.write(expected)
            port.expect(expected)
        }

        internal fun getInfo(port: SerialPort): Triple<Int, Int, Int> {
            port.write(0x10)
            port.expect(0x10)
            val productCode = port.readInt()
            val appVersion = port.readInt()
            val bootVersion = port.readInt()

            return Triple(productCode, appVersion, bootVersion)
        }
    }

    override fun upgradeReceiver(task: FirmwareUpgradeService.FirmwareUpgradeTask, port: SerialPort) {
        port.setup()
        port.use {
            val (productCode, appVersion, bootVersion) = try {
                // try if receiver is already in update mode
                getInfo(it)
            } catch (e: IOException) {
                // wait for receiver boot
                task.print(messages["waitForReceiver"])
                val rc = it.waitForBoot(task, 0x05, 0x0a)
                if (task.isCancelled) {
                    task.print(messages["cancelled"])
                    return@use
                }

                // switch receiver into boot mode
                upgradeMode(rc, it)
                Thread.sleep(100)

                // read product code and versions
                getInfo(it)
            }

            try {
                if (productCode != receiverType.productCode) throw IOException(String.format(messages["invalidReceiverType"], ReceiverType.forProductCode(productCode), receiverType))

                val latch = CountDownLatch(1)
                var doUpdate = false

                runLater {
                    confirm(messages["currentVersion"], String.format(messages["versionInfo"], receiverType, version(appVersion), version(bootVersion), version(version))) {
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

                        if (task.isCancelled) {
                            task.print(messages["cancelled"])
                            return@use
                        }

                        val response = ByteArray(data.size)
                        it.writeBytes(data)
                        it.readBytes(response)
                        if (!response.contentEquals(data)) throw IOException(messages["transmissionError"]) // TODO implement re-try

                        it.expect(0x01)
                    }

                    task.print(messages["done"])
                } else {
                    task.print(messages["cancelled"])
                    task.cancel()
                }
            } catch (e: IOException) {
                task.print(messages["transmissionError"])
                throw e
            }
        }
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

private fun version(v: Int) = "v" + (v.toDouble() / 1000.0).toString().replace(",", ".")
