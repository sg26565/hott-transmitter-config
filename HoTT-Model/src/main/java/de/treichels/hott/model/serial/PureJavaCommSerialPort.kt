package de.treichels.hott.model.serial

import de.treichels.hott.util.Util
import purejavacomm.CommPortIdentifier
import purejavacomm.SerialPort
import purejavacomm.SerialPortEvent
import purejavacomm.SerialPortEventListener

class PureJavaCommSerialPort(portName: String) : SerialPortBase<SerialPort>(portName), SerialPortEventListener {
    override fun serialEvent(event: SerialPortEvent?) {
        val type = when(event?.eventType) {
            SerialPortEvent.BI -> "Break interrupt"
            SerialPortEvent.CD -> "Carrier detect"
            SerialPortEvent.CTS -> "Clear to send"
            SerialPortEvent.DATA_AVAILABLE -> "Data available"
            SerialPortEvent.DSR -> "Date set ready"
            SerialPortEvent.FE -> "Framing error"
            SerialPortEvent.OE -> "Overrun error"
            SerialPortEvent.OUTPUT_BUFFER_EMPTY -> "Output buffer is empty"
            SerialPortEvent.PE -> "Parity error"
            SerialPortEvent.RI -> "Ring indicator"
            else -> "null"
        }

        debug("serialEvent", "eventType=$type oldValue=${event?.oldValue} newValue=${event?.newValue}")

        when(event?.eventType) {
            SerialPortEvent.DATA_AVAILABLE -> readFromPort()
            SerialPortEvent.OUTPUT_BUFFER_EMPTY -> writeToPort()
        }
    }

    private val identifier: CommPortIdentifier = CommPortIdentifier.getPortIdentifier(portName)

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
            notifyOnBreakInterrupt(true)
            notifyOnCarrierDetect(true)
            notifyOnCTS(true)
            notifyOnDataAvailable(true)
            notifyOnDSR(true)
            notifyOnFramingError(true)
            notifyOnOutputEmpty(true)
            notifyOnOverrunError(true)
            notifyOnParityError(true)
            notifyOnRingIndicator(true)
        }
    }

    override fun reset() {
        // do nothing
    }

    override fun readFromPort() {
        while (true) {
            val bytes = port?.inputStream?.readBytes()
            if (bytes != null && bytes.isNotEmpty()) {
                debug("readFromPort", "${bytes.size} bytes available\n${Util.dumpData(bytes)}")
                bytes.forEach { readQueue.put(it) }
            } else {
                debug("readFromPort", "no more data available")
                break;
            }
        }
    }

    override fun writeToPort() {
        val available = writeQueue.size

        if (available > 0) {
            val bytes = ByteArray(available) {
                writeQueue.take()
            }
            debug("writeToPort", "$available bytes to write\n${Util.dumpData(bytes)}")
            port?.outputStream?.write(bytes)
        } else {
            debug("writeToPort", "write buffer is empty")
        }
    }

    companion object {
        val availablePorts: List<String>
            get() = CommPortIdentifier.getPortIdentifiers().toList().filter { it.portType == CommPortIdentifier.PORT_SERIAL }.map { it.name }
    }
}