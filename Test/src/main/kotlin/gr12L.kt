import com.fazecast.jSerialComm.SerialPort
import de.treichels.hott.update.*
import java.io.File
import java.io.IOException

fun main(vararg args: String) {
    //LogManager.getLogManager().readConfiguration(ClassLoader.getSystemResourceAsStream("logging.properties"))
    val port = SerialPort.getCommPort("COM3")
    val fileName1 = "C:/Users/olive/.java/cache/HoTT/firmware/gr12l/GR-12L_1a92.bin"
    val fileName2 = "C:/Users/olive/.java/cache/HoTT/firmware/gr16/FS_GR-16_7a06.bin"
    val firmware = DeviceFirmware.load(File(fileName1))
    var rc: Int

    port.setComPortParameters(19200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY)
    port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING + SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0)
    port.openPort()


    println("waiting for receiver boot")
    while (true) {
        rc = port.read()
        if (rc == 0x05 || rc == 0x0a) break
    }

    if (rc == 0x05) rc = 0x09
    port.write(rc)
    port.expect(rc)

    Thread.sleep(100)
    port.write(0x09)
    port.expect(0x09)
    val flag1 = port.readInt()
    val flag2 = port.readInt()
    println("Flag1=0x${flag1.toString(16)} ($flag1), Flag2=0x${flag2.toString(16)} ($flag2)")

    Thread.sleep(100)
    port.write(0x10)
    port.expect(0x10)
    val productCode = port.readInt()
    val appVersion = port.readInt()
    val bootVersion = port.readInt()

    println("ProductCode=$productCode, AppVersion=$appVersion, BootVersion=$bootVersion")

    if (productCode != firmware.deviceType.productCode) throw IOException("invalid receiver type")

    firmware.packets.forEachIndexed { index, data ->
        val buffer = ByteArray(data.size)
        println("write packet $index")
        port.writeBytes(data)
        port.readBytes(buffer)

        if (!buffer.contentEquals(data)) throw IOException("packet write error")

        port.expect(0x01)
    }

    println("done")
}
