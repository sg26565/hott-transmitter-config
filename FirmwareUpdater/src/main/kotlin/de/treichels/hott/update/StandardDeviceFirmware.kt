package de.treichels.hott.update

import com.fazecast.jSerialComm.SerialPort
import de.treichels.hott.model.enums.ReceiverType
import de.treichels.hott.model.enums.Registered
import de.treichels.hott.util.readInt
import de.treichels.hott.util.readUInt
import de.treichels.hott.util.readUShort
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import tornadofx.*
import java.io.File
import java.io.IOException
import java.util.concurrent.CountDownLatch

class StandardDeviceFirmware(deviceType: Registered<*>, val version: Int, val deviceId: Int, val processId: Int, packets: Array<ByteArray>) : DeviceFirmware(deviceType, packets) {
        companion object {
        val devicesNeedButtonBoot = listOf<Registered<*>>(ReceiverType.gr12, ReceiverType.gr12s, ReceiverType.gr16, ReceiverType.gr24, ReceiverType.gr32)

        fun load(file: File): StandardDeviceFirmware {
            file.inputStream().use { stream ->
                // read file header
                val flag1 = stream.readUInt()
                if (flag1 != 0x23456789u && flag1 != 0x12345678u) throw IOException(messages["invalidFileHeader"])

                val flag2 = stream.readUInt()
                if (0xffffffffu - flag2 != flag1) throw IOException(messages["invalidFileHeader"])

                val deviceId = stream.readInt() and 0x00ff
                val processId = if (deviceId == 0x1008) stream.readInt() and 0xff else (stream.readInt() shr 4) and 0x0f

                val productCode = stream.readInt()
                val deviceType: Registered<*> = deviceList.forProductCode(productCode)
                        ?: throw IOException(String.format(messages["unknownDevice"], productCode))

                val version = stream.readInt()
                val packetCount = stream.readInt()

                stream.skip(36)

                val packets = Array(packetCount) {
                    val flag = stream.read()
                    if (flag != 0) throw IOException(messages["invalidBlock"])

                    val seq1 = stream.readUShort()
                    val seq2 = stream.readUShort()
                    if (seq1.toUInt() != 0xffffu - seq2) throw IOException(messages["invalidBlock"])

                    val size = stream.readUShort()
                    stream.skip(9)

                    val data = ByteArray(size.toInt())
                    stream.read(data)

                    data
                }

                return StandardDeviceFirmware(deviceType, version, deviceId, processId, packets)
            }
        }

        internal fun updateMode(rc: Int, port: SerialPort) {
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

    override fun updateDevice(task: FirmwareUpdateService.FirmwareUpdateTask, port: SerialPort) {
        port.setup()
        port.use {
            val (productCode, appVersion, bootVersion) = try {
                // try if device is already in update mode
                getInfo(it)
            } catch (e: IOException) {
                if (deviceType in devicesNeedButtonBoot) {
                    // hold down bind button during boot
                    task.print(messages["waitForDeviceButton"])
                } else {
                    // normal boot
                    task.print(messages["waitForDevice"])
                }

                val rc = it.waitForBoot(task, 0x05, 0x0a)
                if (task.isCancelled) {
                    task.print(messages["cancelled"])
                    return@use
                }

                // switch device into boot mode
                updateMode(rc, it)
                Thread.sleep(100)

                // read product code and versions
                getInfo(it)
            }

            try {
                if (productCode != deviceType.productCode) throw IOException(String.format(messages["invalidDeviceType"], ReceiverType.forProductCode(productCode), deviceType))

                val latch = CountDownLatch(1)
                var doUpdate = false

                runLater {
                    if (version < appVersion)
                        warning(messages["downgradeWarning"], String.format(messages["downgrageText"], version(appVersion), version(version)), ButtonType(messages["doDowngrade"], ButtonBar.ButtonData.APPLY), ButtonType.CANCEL) {
                            doUpdate = (result.buttonData == ButtonBar.ButtonData.APPLY)
                        }
                    else
                        confirm(messages["currentVersion"], String.format(messages["versionInfo"], deviceType, version(appVersion), version(bootVersion), version(version))) {
                            doUpdate = true
                        }
                    latch.countDown()
                }

                latch.await()

                if (doUpdate) {
                    val packetCount = packets.size
                    packets.forEachIndexed { index, data ->
                        var retries = 0

                        while (true) {
                            try {
                                task.progress(index + 1, packetCount)
                                task.print(String.format(messages["writePacket"], index + 1, packetCount))

                                if (task.isCancelled) {
                                    task.print(messages["cancelled"])
                                    return@use
                                }

                                // write block to device
                                val response = ByteArray(data.size)
                                it.writeBytes(data)
                                it.readBytes(response)
                                if (!response.contentEquals(data)) throw IOException(messages["transmissionError"])
                                it.expect(0x01)

                                break
                            } catch (e: IOException) {
                                // retry on error up to retryCount times
                                if (retries++ >= retryCount) throw e
                            }
                        }
                    }

                    // end transfer
                    it.write(8)
                    it.expect(8)

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

        other as StandardDeviceFirmware

        if (deviceType != other.deviceType) return false
        if (version != other.version) return false
        if (!packets.contentDeepEquals(other.packets)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = deviceType.hashCode()
        result = 31 * result + version
        result = 31 * result + packets.contentDeepHashCode()
        return result
    }

    override fun toString(): String {
        return "StandardDeviceFirmware($deviceType, v${version / 1000.0}, productCode=${deviceType.productCode}, deviceID=$deviceId, processId=$processId)"
    }
}

private fun version(v: Int) = "v" + (v.toDouble() / 1000.0).toString().replace(",", ".")
