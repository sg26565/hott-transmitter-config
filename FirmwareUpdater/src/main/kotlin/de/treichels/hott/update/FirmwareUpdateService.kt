package de.treichels.hott.update

import com.fazecast.jSerialComm.SerialPort
import javafx.concurrent.Service
import javafx.concurrent.Task
import tornadofx.*
import java.io.IOException
import java.lang.IllegalArgumentException

class FirmwareUpdateService : Service<Unit>() {
    lateinit var serialPort: SerialPort
    lateinit var fileName: String

    override fun createTask() = FirmwareUpdateTask()

    inner class FirmwareUpdateTask : Task<Unit>() {
        override fun call() {
            DeviceFirmware.load(fileName).updateDevice(this, serialPort)
        }

        fun print(newMessage: String) {
            runLater {
                updateMessage(newMessage)
            }
        }

        fun progress(done: Int, total: Int) {
            updateProgress(done.toLong(), total.toLong())
        }
    }
}

fun <T> SerialPort.use(func: (port: SerialPort) -> T): T {
    try {
        openPort()
        return func(this)
    } finally {
        closePort()
    }
}

fun SerialPort.readBytes(data: ByteArray) = readBytes(data, data.size.toLong(), 0L)
fun SerialPort.writeBytes(data: ByteArray) = writeBytes(data, data.size.toLong(), 0L)
fun SerialPort.read(): Int {
    val buffer = ByteArray(1)
    val rc = readBytes(buffer)
    if (rc != 1) throw IOException(DeviceFirmware.messages["transmissionError"])
    return buffer[0].toInt() and 0xff
}

fun SerialPort.write(b: Int) {
    if (b < 0 || b > 255) throw IllegalArgumentException("$b is not in range 0..255")
    val buffer = byteArrayOf(b.toByte())
    val rc = writeBytes(buffer)
    if (rc != 1) throw IOException(DeviceFirmware.messages["transmissionError"])
}

fun SerialPort.readInt(): Int {
    val buffer = ByteArray(4)
    val rc = readBytes(buffer)
    if (rc != 4) throw IOException(DeviceFirmware.messages["transmissionError"])

    var result: Long = 0

    result += buffer[0].toLong() and 0xff
    result += (buffer[1].toLong() and 0xff) shl 8
    result += (buffer[2].toLong() and 0xff) shl 16
    result += (buffer[3].toLong() and 0xff) shl 24

    return result.toInt()
}

fun SerialPort.expect(rc: Int) {
    val b = read()
    if (b != rc) throw IOException(String.format(DeviceFirmware.messages["invalidResponse"], b, rc))
}

fun SerialPort.setup(baudRate: Int = 19200) {
    setComPortParameters(baudRate, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY)
    setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING + SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 1000)
}

fun SerialPort.waitForBoot(task: Task<*>, vararg expected: Int) : Int {
    // wait for device boot
    while (true) {
        try {
            val rc = read()
            if (rc in expected) return  rc
        } catch (e: IOException) {
            if (task.isCancelled) return 0
        }
    }
}
