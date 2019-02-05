import de.treichels.hott.decoder.internal.firmware.DeviceFirmware
import de.treichels.hott.decoder.internal.firmware.StandardDeviceFirmware
import de.treichels.hott.decoder.internal.firmware.waitForBoot
import de.treichels.hott.serial.SerialPort
import java.io.File
import java.io.IOException

fun main() {
    //LogManager.getLogManager().readConfiguration(ClassLoader.getSystemResourceAsStream("logging.properties"))
    val port = SerialPort.getPort("COM3")
    val fileName1 = "C:/Users/olive/.java/cache/HoTT/firmware/gr12l/GR-12L_1a92.bin"
    //val fileName2 = "C:/Users/olive/.java/cache/HoTT/firmware/gr16/FS_GR-16_7a06.bin"
    val firmware = DeviceFirmware.load(File(fileName1))

    port.baudRate=19200
    port.timeout=1000
    port.use {
        it.open()

        println("waiting for receiver boot")
        val rc = it.waitForBoot(null, 0x05, 0x0a)

        StandardDeviceFirmware.updateMode(rc, it)

        Thread.sleep(100)
        val (deviceId, processId) = StandardDeviceFirmware.getDeviceId(it)
        println("DeviceId=$deviceId, ProcessId=$processId")

        Thread.sleep(100)
        val (productCode, appVersion,bootVersion) = StandardDeviceFirmware.getInfo(it)
        println("ProductCode=$productCode, AppVersion=$appVersion, BootVersion=$bootVersion")

        if (productCode != firmware.deviceType.productCode) throw IOException("invalid receiver type")

        val packetCount = firmware.packets.size
        firmware.packets.forEachIndexed { index, data ->
            val buffer = ByteArray(data.size)
            println("write packet $index of $packetCount")
            it.writeBytes(data)
            it.readBytes(buffer)

            if (!buffer.contentEquals(data)) throw IOException("packet write error")

            it.expect(0x01)
        }

        println("done")
    }
}
