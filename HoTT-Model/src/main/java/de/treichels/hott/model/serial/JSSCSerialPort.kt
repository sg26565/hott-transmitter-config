/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package de.treichels.hott.model.serial

import de.treichels.hott.model.HoTTException
import de.treichels.hott.util.Util
import jssc.SerialPort.*
import jssc.SerialPortEvent
import jssc.SerialPortEventListener
import jssc.SerialPortException
import jssc.SerialPortList
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
class JSSCSerialPort (override var portName: String) : SerialPort, SerialPortEventListener {
    private val readBuffer = ByteBuffer(READ_BUFFER_SIZE)
    private val writeBuffer = ByteBuffer(WRITE_BUFFER_SIZE)

    /** The internal low-level serial port implementation.  */
    private var port: jssc.SerialPort? = null

    override val inputStream: InputStream
        get() = SerialPortInputStream()

    override val outputStream: OutputStream
        get() = SerialPortOutPutStream()

    override val isOpen: Boolean
        get() = port != null && port!!.isOpened

    private inner class SerialPortInputStream : InputStream() {
        override fun available(): Int {
            return readBuffer.available()
        }


        override fun read(): Int {
            // block until data is available
            while (readBuffer.available() == 0) {
                try {
                    readFromPort()
                } catch (e: SerialPortException) {
                    throw IOException(e)
                }

                if (readBuffer.available() == 0) readBuffer.waitRead(1)
            }

            return readBuffer.read()
        }
    }

    private inner class SerialPortOutPutStream : OutputStream() {

        override fun flush() {
            try {
                writeToPort()
            } catch (e: SerialPortException) {
                throw IOException(e)
            }

        }

        override fun write(b: Int) {
            if (writeBuffer.remaining() == 0) {
                flush()
                writeBuffer.waitWrite(1)
            }

            writeBuffer.write(b and 0xff)
        }
    }

    @Throws(HoTTException::class)
    override fun close() {
        try {
            if (isOpen) port!!.closePort()
        } catch (e: SerialPortException) {
            throw HoTTException(e)
        } finally {
            port = null
        }
    }

    @Throws(HoTTException::class)
    override fun open() {
        if (isOpen) throw HoTTException("HoTTSerialPort.AlreadyOpen")

        try {
            port = jssc.SerialPort(portName)
            port!!.openPort()
            port!!.setParams(BAUDRATE_115200, DATABITS_8, STOPBITS_1, PARITY_NONE, false, false)
            port!!.flowControlMode = FLOWCONTROL_NONE
            port!!.addEventListener(this, MASK_RXCHAR + MASK_TXEMPTY)
        } catch (e: SerialPortException) {
            throw HoTTException(e)
        }

    }

    @Synchronized
    @Throws(SerialPortException::class)
    private fun readFromPort() {
        if (DEBUG) System.out.printf("readFromPort: %d bytes available%n", port!!.inputBufferBytesCount)

        val bytes = port!!.readBytes()
        if (DEBUG) println(Util.dumpData(bytes))

        if (bytes != null) readBuffer.write(bytes)
    }

    @Throws(HoTTException::class)
    override fun reset() {
        try {
            readBuffer.reset()
            writeBuffer.reset()
            port!!.purgePort(PURGE_RXABORT + PURGE_RXCLEAR + PURGE_TXABORT + PURGE_TXCLEAR)
        } catch (e: SerialPortException) {
            throw HoTTException(e)
        }

    }

    override fun serialEvent(event: SerialPortEvent) {
        if (DEBUG)
            System.out.printf("serialEvent: port=%s, type=%s(%d), value=%d%n", event.portName, if (event.eventType == 1) "RXCHAR" else "TXEMPTY",
                    event.eventType, event.eventValue)

        try {
            if (event.isRXCHAR) readFromPort()

            if (event.isTXEMPTY) writeToPort()
        } catch (e: SerialPortException) {
            throw RuntimeException(e)
        }

    }

    @Synchronized
    @Throws(SerialPortException::class)
    private fun writeToPort() {
        val available = writeBuffer.available()

        if (available > 0) {
            if (DEBUG) System.out.printf("writeToPort: %d bytes to write%n", available)
            // write max 512 bytes to serial port
            val bytes = ByteArray(Math.min(available, 512))
            writeBuffer.read(bytes)
            if (DEBUG) println(Util.dumpData(bytes))
            port!!.writeBytes(bytes)
        }
    }

    companion object {
        private val DEBUG = Util.DEBUG
        private const val READ_BUFFER_SIZE = 4096
        private const val WRITE_BUFFER_SIZE = 4096

        val availablePorts: List<String>
            get() = Arrays.asList(*SerialPortList.getPortNames())
    }
}
