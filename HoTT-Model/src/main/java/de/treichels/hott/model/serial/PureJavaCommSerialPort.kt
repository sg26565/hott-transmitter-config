package de.treichels.hott.model.serial

import de.treichels.hott.util.Util
import jtermios.JTermios
import purejavacomm.CommPortIdentifier
import purejavacomm.SerialPort
import purejavacomm.SerialPortEvent
import purejavacomm.SerialPortEventListener

class PureJavaCommSerialPort(portName: String) : SerialPortBase(portName), SerialPortEventListener {
    override fun serialEvent(event: SerialPortEvent?) {
        val type = when (event?.eventType) {
            SerialPortEvent.DATA_AVAILABLE -> {
                readFromPort()
                "Data available"
            }

            SerialPortEvent.OUTPUT_BUFFER_EMPTY -> {
                writeToPort()
                "Output buffer is empty"
            }

            else -> "unknown event"
        }

        logger.finer("eventType=$type oldValue=${event?.oldValue} newValue=${event?.newValue}")
    }

    private val identifier: CommPortIdentifier = CommPortIdentifier.getPortIdentifier(portName)
    private var port: SerialPort? = null

    init {
        JTermios.JTermiosLogging.setLogLevel(0)
    }

    override val isOpen: Boolean
        get() = port != null

    override fun close() {
        port?.close()
        port = null
    }

    override fun open() {
        port = (identifier.open("VDFEditor", 1000) as SerialPort).apply {
            setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)
            flowControlMode = SerialPort.FLOWCONTROL_NONE
            addEventListener(this@PureJavaCommSerialPort)
            notifyOnDataAvailable(true)
            notifyOnOutputEmpty(true)
        }
    }

    override fun reset() {
        // do nothing
    }

    @Synchronized
    override fun readFromPort() {
        val inputStream = port?.inputStream
        val available = Math.min(inputStream?.available() ?: 0, 2048)

        if (available > 0) {
            val bytes = ByteArray(available)
            inputStream?.read(bytes)
            logger.finer("${bytes.size} bytes available\n${Util.dumpData(bytes)}")
            bytes.forEach { readQueue.put(it) }
        } else {
            logger.finest("no more data available")
        }
    }

    @Synchronized
    override fun writeToPort() {
        val available = writeQueue.size
        val outputStream = port?.outputStream

        if (available > 0) {
            val bytes = ByteArray(available) {
                writeQueue.take()
            }
            logger.finer("$available bytes to write\n${Util.dumpData(bytes)}")
            outputStream?.write(bytes)
        } else {
            logger.finest("write buffer is empty")
        }
    }

    companion object {
        val availablePorts: List<String>
            get() = JTermios.getPortList()
    }
}