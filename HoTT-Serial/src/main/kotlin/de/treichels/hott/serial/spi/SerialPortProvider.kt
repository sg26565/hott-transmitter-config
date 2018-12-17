package de.treichels.hott.serial.spi

import de.treichels.hott.serial.SerialPort

interface SerialPortProvider {
    fun getAvailablePorts(): List<String>
    fun getPort(portName: String): SerialPort
}