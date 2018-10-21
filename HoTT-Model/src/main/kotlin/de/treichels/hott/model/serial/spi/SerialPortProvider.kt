package de.treichels.hott.model.serial.spi

import de.treichels.hott.model.serial.SerialPort

interface SerialPortProvider {
    fun getAvailablePorts(): List<String>
    fun getPort(portName: String): SerialPort
}