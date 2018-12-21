package de.treichels.hott.serial

import de.treichels.hott.model.HoTTException
import de.treichels.hott.serial.spi.SerialPortProvider
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

abstract class SerialPortBase(override val portName: String) : SerialPort {
    override var timeout: Int = 500

    protected val logger: Logger
        get() = Logger.getLogger(javaClass.name)

    /** read buffer */
    protected val readQueue: BlockingQueue<Byte> = ArrayBlockingQueue(READ_BUFFER_SIZE, true)

    /** write buffer */
    protected val writeQueue: BlockingQueue<Byte> = ArrayBlockingQueue(WRITE_BUFFER_SIZE, true)

    override val inputStream: InputStream
        get() = SerialPortInputStream()

    override val outputStream: OutputStream
        get() = SerialPortOutPutStream()

    protected inner class SerialPortInputStream : InputStream() {
        override fun available() = readQueue.size

        override fun read(): Int {
            if (readQueue.size == 0) readFromPort()

            // block until data is available

            val b: Byte? = if (timeout > 0) readQueue.poll(timeout.toLong(), TimeUnit.MILLISECONDS) else readQueue.take()
            return if (b == null) {
                throw HoTTException("read timeout")
            } else {
                b.toInt() and 0xff
            }
        }
    }

    protected inner class SerialPortOutPutStream : OutputStream() {
        override fun flush() {
            writeToPort()
        }

        override fun write(b: Int) {
            writeQueue.put((b and 0xff).toByte())
        }
    }

    abstract fun readFromPort()
    abstract fun writeToPort()

    companion object {
        private const val READ_BUFFER_SIZE = 4096
        private const val WRITE_BUFFER_SIZE = 4096
        private val provider: SerialPortProvider by lazy {
            ServiceLoader.load(SerialPortProvider::class.java).first()
        }

        fun getAvailablePorts(): List<String> = provider.getAvailablePorts()
        fun getPort(name: String): SerialPort = provider.getPort(name)
    }
}
