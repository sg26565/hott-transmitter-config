package de.treichels.hott.serial

import de.treichels.hott.model.serial.SerialPort
import de.treichels.hott.model.serial.spi.SerialPortProvider

class JSerialCommProvider: SerialPortProvider {
    override fun getAvailablePorts(): List<String> = com.fazecast.jSerialComm.SerialPort.getCommPorts().map { it.systemPortName }
    override fun getPort(portName: String): SerialPort = JSerialCommPort(portName)
}