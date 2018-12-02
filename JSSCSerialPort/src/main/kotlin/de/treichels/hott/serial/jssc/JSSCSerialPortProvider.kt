package de.treichels.hott.serial.jssc

import de.treichels.hott.serial.SerialPort
import de.treichels.hott.serial.spi.SerialPortProvider

class JSSCSerialPortProvider: SerialPortProvider {
    override fun getAvailablePorts(): List<String> = jssc.SerialPortList.getPortNames().toList()
    override fun getPort(portName: String): SerialPort = JSSCSerialPort(portName)
}