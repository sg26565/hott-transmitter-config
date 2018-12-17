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
package de.treichels.hott.serial.jssc

import de.treichels.hott.model.HoTTException
import de.treichels.hott.serial.SerialPortBase
import de.treichels.hott.util.Util
import jssc.SerialPort.*
import jssc.SerialPortEvent
import jssc.SerialPortEventListener

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
class JSSCSerialPort(portName: String) : SerialPortBase(portName), SerialPortEventListener {
    private var port: jssc.SerialPort? = null

    override val isOpen: Boolean
        get() = port?.isOpened ?: false

    override fun close() {
        try {
            if (isOpen) port?.closePort()
        } finally {
            port = null
        }
    }

    override fun open(baudRate: Int) {
        if (isOpen) throw HoTTException("HoTTSerialPort.AlreadyOpen")

        val port = jssc.SerialPort(portName)
        port.openPort()
        port.setParams(baudRate, DATABITS_8, STOPBITS_1, PARITY_NONE, false, false)
        port.flowControlMode = FLOWCONTROL_NONE
        port.addEventListener(this, MASK_RXCHAR + MASK_TXEMPTY)

        this.port = port
    }

    @Synchronized
    override fun readFromPort() {
        val bytes = port?.readBytes()
        if (bytes != null && bytes.isNotEmpty()) {
            logger.finer("readFromPort: ${bytes.size} bytes available\n${Util.dumpData(bytes)}")
            bytes.forEach { readQueue.put(it) }
        } else {
            logger.finest("readFromPort: no more data available")
        }
    }

    override fun reset() {
        readQueue.clear()
        writeQueue.clear()
        port?.purgePort(PURGE_RXABORT + PURGE_RXCLEAR + PURGE_TXABORT + PURGE_TXCLEAR)
    }

    override fun serialEvent(event: SerialPortEvent) {
        val type = if (event.eventType == 1) "RXCHAR" else "TXEMPTY"
        logger.finest("serialEvent: " + with(event) { "port=$portName, type=$type, value=$eventValue" })

        if (event.isRXCHAR) readFromPort()
        if (event.isTXEMPTY) writeToPort()
    }

    @Synchronized
    override fun writeToPort() {
        val available = writeQueue.size

        if (available > 0) {
            val bytes = ByteArray(available) {
                writeQueue.take()
            }
            logger.finer("writeToPort: $available bytes to write\n${Util.dumpData(bytes)}")
            port?.writeBytes(bytes)
        } else {
            logger.finest("writeToPort: write buffer is empty")
        }
    }
}
