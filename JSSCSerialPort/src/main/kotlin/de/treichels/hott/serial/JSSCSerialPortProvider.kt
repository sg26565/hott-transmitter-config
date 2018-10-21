package de.treichels.hott.serial

import de.treichels.hott.model.serial.SerialPort
import de.treichels.hott.model.serial.spi.SerialPortProvider

class JSSCSerialPortProvider: SerialPortProvider {
    override fun getAvailablePorts(): List<String> = jssc.SerialPortList.getPortNames().toList()
    override fun getPort(portName: String): SerialPort = JSSCSerialPort(portName)
}