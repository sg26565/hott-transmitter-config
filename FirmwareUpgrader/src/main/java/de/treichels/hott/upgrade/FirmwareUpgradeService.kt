package de.treichels.hott.upgrade

import de.treichels.hott.model.HoTTException
import de.treichels.hott.model.serial.SerialPort
import javafx.concurrent.Service
import javafx.concurrent.Task
import tornadofx.*
import java.io.File
import java.nio.file.Files
import java.util.*

class FirmwareUpgradeService : Service<Unit>() {
    lateinit var serialPort: SerialPort
    lateinit var fileName: String
    private var messages = ResourceBundle.getBundle(javaClass.name)

    override fun createTask(): Task<Unit> = object : Task<Unit>() {
        override fun call() {
            val file = File(fileName)

            if (!(file.exists() && file.isFile && file.canRead()))
                throw NoSuchFileException(file)

            val size = file.length()
            val headerSize = 8
            val blockSize = 0x406
            val blockCount = (size.toInt() - headerSize) / blockSize
            val bytes = Files.readAllBytes(file.toPath())

            serialPort.use { port ->
                port.timeout = 1000
                port.open(19200)
                val fileStream = bytes.inputStream()
                val inputStream = port.inputStream
                val outputStream = port.outputStream

                //header
                val type = fileStream.read()
                fileStream.skip(7)

                // wait for receiver
                updateMessage(messages["waitForReceiver"])

                do {
                    try {
                        val byte = inputStream.read()
                        if (byte == type) break
                    } catch (e: HoTTException) {
                        if (isCancelled) return
                    }
                } while (true)

                // write data
                for (i in 1..blockCount) {
                    updateProgress(i.toLong(), blockCount.toLong())
                    updateMessage("${(i.toDouble() / blockCount.toDouble() * 100.0).toInt()} %")

                    // read block from file
                    val block = ByteArray(blockSize)
                    fileStream.read(block)
                    if (isCancelled) return

                    // write block to receiver
                    outputStream.write(block)
                    outputStream.flush()
                    if (isCancelled) return

                    // read response
                    val response = ByteArray(blockSize)
                    inputStream.read(response)
                    if (isCancelled) return

                    // read status
                    val byte = inputStream.read()

                    if (!(response contentEquals block && byte == 0xaa)) throw HoTTException(messages["transmissionError"])
                    if (isCancelled) return
                }

                // end transfer
                val block = ByteArray(blockSize)
                block[2] = 0xee.toByte()
                outputStream.write(block)
                outputStream.flush()
            }
        }
    }
}