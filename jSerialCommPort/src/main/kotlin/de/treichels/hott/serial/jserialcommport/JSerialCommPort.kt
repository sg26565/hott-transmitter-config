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
package de.treichels.hott.serial.jserialcommport

import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPort.*
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import de.treichels.hott.model.HoTTException
import de.treichels.hott.serial.SerialPortBase
import de.treichels.hott.util.Util

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
class JSerialCommPort(portName: String) : SerialPortBase(portName), SerialPortDataListener {
    private var port: SerialPort

    override var baudRate
        get() = port.baudRate
        set(baudRate) {
            port.setComPortParameters(baudRate, 8, ONE_STOP_BIT, NO_PARITY)
        }

    override var readTimeout
        get() = port.readTimeout
        set(readTimeout) {
            setTimeouts(readTimeout, writeTimeout)
        }

    override var writeTimeout
        get() = port.writeTimeout
        set(writeTimeout) {
            setTimeouts(readTimeout, writeTimeout)
        }

    init {
        port = SerialPort.getCommPort(portName)
        port.setFlowControl(FLOW_CONTROL_DISABLED)

        // default to blocking read and write with 1 second timeout
        setTimeouts(1000, 1000)

        // default 115200 baud, 8 data bits, one stop bit, no parity
        baudRate = 115200
    }

    private fun setTimeouts(readTimeout: Int, writeTimeout: Int) {
        val mode = if (readTimeout < 0) TIMEOUT_NONBLOCKING else TIMEOUT_READ_BLOCKING +
                if (writeTimeout < 0) TIMEOUT_NONBLOCKING else TIMEOUT_WRITE_BLOCKING

        port.setComPortTimeouts(mode, readTimeout, writeTimeout)
    }

    override fun readBytes(data: ByteArray, length: Long, offset: Long) = port.readBytes(data, length, offset)

    override fun writeBytes(data: ByteArray, length: Long, offset: Long) = port.writeBytes(data, length, offset)

    override fun getListeningEvents(): Int = LISTENING_EVENT_DATA_AVAILABLE or LISTENING_EVENT_DATA_WRITTEN

    override val isOpen: Boolean
        get() = port.isOpen

    override fun close() {
        if (isOpen) port.closePort()
    }

    override fun open() {
        if (isOpen) throw HoTTException("HoTTSerialPort.AlreadyOpen")

        //port.addDataListener(this)
        port.openPort(0)
        if (!port.isOpen) {
            port.disablePortConfiguration()
            port.openPort(0)
        }
    }

    @Synchronized
    override fun readFromPort() {
        val available = port.bytesAvailable()
        if (available > 0) {
            val bytes = ByteArray(available)
            port.readBytes(bytes, available.toLong())
            logger.finer("readFromPort: ${bytes.size} bytes available\n${Util.dumpData(bytes)}")
            bytes.forEach { readQueue.put(it) }
        } else {
            logger.finest("readFromPort: no more data available")
        }
    }

    override fun reset() {
        readQueue.clear()
        writeQueue.clear()
    }

    private val SerialPortEvent.eventName: String
        get() = when (eventType) {
            LISTENING_EVENT_DATA_AVAILABLE -> "Data available"
            LISTENING_EVENT_DATA_WRITTEN -> "Data written"
            LISTENING_EVENT_DATA_RECEIVED -> "Data received"
            else -> "Unknown event"
        }

    override fun serialEvent(event: SerialPortEvent) {
        logger.finest("serialEvent: port=$portName, event=${event.eventName}")

        when (event.eventType) {
            LISTENING_EVENT_DATA_WRITTEN -> writeToPort()
            LISTENING_EVENT_DATA_AVAILABLE -> readFromPort()
        }
    }

    @Synchronized
    override fun writeToPort() {
        val available = writeQueue.size

        if (available > 0) {
            val bytes = ByteArray(available) {
                writeQueue.take()
            }
            logger.finer("writeToPort: $available bytes to write\n${Util.dumpData(bytes)}")
            port.writeBytes(bytes, available.toLong())
        } else {
            logger.finest("writeToPort: write buffer is empty")
        }
    }
}
