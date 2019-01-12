import de.treichels.hott.model.HoTTException
import de.treichels.hott.serial.SerialPort
import de.treichels.hott.serial.SerialPortBase
import de.treichels.hott.util.Util
import java.io.InputStream
import java.io.OutputStream

private lateinit var serialPort: SerialPort
private lateinit var inputStream: InputStream
private lateinit var outputStream: OutputStream

fun main(args: Array<String>) {
    //val availablePorts = JSSCSerialPort.availablePorts

    serialPort = SerialPortBase.getPort("COM3")
    serialPort.use { port ->
        port.open()
        port.baudRate = 19200
        port.timeout = 1000
        inputStream = port.inputStream
        outputStream = port.outputStream

        // wait for boot
        println("wait for boot")
        waitFor(5)

        println("boot detected")

        // write boote response
        write(9)
        read()
        println("response sent")

        Thread.sleep(100)

        write(9)
        read()
        Thread.sleep(100)

        val buffer = ByteArray(8)
        inputStream.read(buffer)

        println(Util.dumpData(buffer))

        val deviceId = ((buffer[1].toInt() shl 8) and 0xFF00) + (buffer[0].toInt() and 0xFF)
        println("deviceId = $deviceId")

        waitFor()
    }
}

fun read() : Int = inputStream.read()

fun write(byte: Int) {
    outputStream.write(byte)
    outputStream.flush()
}

fun waitFor(byte: Int=-1) {
    while (true) {
        try {
            val b = read()
            println(Integer.toHexString(b))
            if (byte == b) break
        } catch (e: HoTTException) {
            print(".")
        }
    }
}
