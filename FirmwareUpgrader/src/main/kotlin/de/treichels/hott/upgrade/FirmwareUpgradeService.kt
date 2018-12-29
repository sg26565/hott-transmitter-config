package de.treichels.hott.upgrade

import com.fazecast.jSerialComm.SerialPort
import de.treichels.hott.model.enums.ReceiverType
import de.treichels.hott.util.Util
import javafx.concurrent.Service
import javafx.concurrent.Task
import tornadofx.*
import java.io.File
import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.*

abstract class ReceiverFirmware(val receiverType: ReceiverType, val packets: Array<ByteArray>) {
    companion object {
        @JvmStatic
        var messages = ResourceBundle.getBundle(ReceiverFirmware::class.java.name)!!

        private val knownIds = ReceiverType.values().map { it.id }.filterNot { it == 0 } + 0x05 + 0x0a

        fun load(fileName: String) = ReceiverFirmware.load(File(fileName))
        fun load(file: File): ReceiverFirmware = try {
            StandardReceiverFirmware.load(file)
        } catch (e1: IOException) {
            try {
                GyroReceiverFirmware.load(file)
            } catch (e2: IOException) {
                throw IOException("${e1.message}/${e2.message}", e2)
            }
        }

        fun detectReceiver(task: Task<*>, port: SerialPort): ReceiverType? = port.use {
            it.setup()

            // wait for receiver boot
            val rc = it.waitForBoot(task, *knownIds.toIntArray())

            if (rc == 0x05 || rc == 0x0a) {
                // standard receiver
                StandardReceiverFirmware.upgradeMode(rc, it)
                Thread.sleep(100)
                val (productCode, _, _) = StandardReceiverFirmware.getInfo(it)
                ReceiverType.forProductCode(productCode)
            } else {
                // gyro receiver
                ReceiverType.forId(rc)
            }
        }
    }

    fun dump(): String {
        val result = StringBuffer()

        result.append(toString()).append('\n')
        result.append("ProductCode: ${receiverType.productCode}\n")
        result.append("ReceiverId: ${receiverType.id}\n")
        result.append("PacketCount: ${packets.size}\n")
        result.append("PacketSize:  ${packets[0].size}\n")
        packets.forEachIndexed { index, bytes ->
            result.append("Packet $index\n")
            result.append(Util.dumpData(bytes))
        }

        return result.toString()
    }

    abstract fun upgradeReceiver(task: FirmwareUpgradeService.FirmwareUpgradeTask, port: SerialPort)
}

class FirmwareUpgradeService : Service<Unit>() {
    lateinit var serialPort: SerialPort
    lateinit var fileName: String

    override fun createTask() = FirmwareUpgradeTask()

    inner class FirmwareUpgradeTask : Task<Unit>() {
        override fun call() {
            ReceiverFirmware.load(fileName).upgradeReceiver(this, serialPort)
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
    if (rc != 1) throw IOException(ReceiverFirmware.messages["transmissionError"])
    return buffer[0].toInt() and 0xff
}

fun SerialPort.write(b: Int) {
    if (b < 0 || b > 255) throw IllegalArgumentException("$b is not in range 0..255")
    val buffer = byteArrayOf(b.toByte())
    val rc = writeBytes(buffer)
    if (rc != 1) throw IOException(ReceiverFirmware.messages["transmissionError"])
}

fun SerialPort.readInt(): Int {
    val buffer = ByteArray(4)
    val rc = readBytes(buffer)
    if (rc != 4) throw IOException(ReceiverFirmware.messages["transmissionError"])

    var result: Long = 0

    result += buffer[0].toLong() and 0xff
    result += (buffer[1].toLong() and 0xff) shl 8
    result += (buffer[2].toLong() and 0xff) shl 16
    result += (buffer[3].toLong() and 0xff) shl 24

    return result.toInt()
}

fun SerialPort.expect(rc: Int) {
    val b = read()
    if (b != rc) throw IOException(String.format(ReceiverFirmware.messages["invalidResponse"], b, rc))
}

fun SerialPort.setup(baudRate: Int = 19200) {
    setComPortParameters(baudRate, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY)
    setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING + SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 1000)
}

fun SerialPort.waitForBoot(task: Task<*>, vararg expected: Int) : Int {
    // wait for receiver boot
    while (true) {
        try {
            val rc = read()
            if (rc in expected) return  rc
        } catch (e: IOException) {
            if (task.isCancelled) return 0
        }
    }
}
