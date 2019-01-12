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
package de.treichels.hott.serial

import de.treichels.hott.model.HoTTException
import de.treichels.hott.serial.spi.SerialPortProvider
import de.treichels.hott.util.ByteOrder
import de.treichels.hott.util.readInt
import de.treichels.hott.util.readLong
import de.treichels.hott.util.readShort
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
interface SerialPort : Closeable {
    val inputStream: InputStream
    val outputStream: OutputStream

    val portName: String
    var baudRate: Int
    val isOpen: Boolean
    var timeout: Int

    @Throws(HoTTException::class)
    override fun close()

    @Throws(HoTTException::class)
    fun open()


    @Throws(HoTTException::class)
    fun reset()

    fun readBytes(data: ByteArray, length: Long = data.size.toLong(), offset: Long = 0L): Int
    fun writeBytes(data: ByteArray, length: Long = data.size.toLong(), offset: Long = 0L): Int

    fun write(b: Int) = writeBytes(byteArrayOf(b.toByte()))
    fun read() = readIntoBuffer(1)[0].toInt() and 0xff
    fun readShort(byteOrder: ByteOrder = ByteOrder.LittleEndian) = readIntoBuffer(Short.SIZE_BYTES).readShort(byteOrder = byteOrder)
    fun readInt(byteOrder: ByteOrder = ByteOrder.LittleEndian) = readIntoBuffer(Int.SIZE_BYTES).readInt(byteOrder = byteOrder)
    fun readLong(byteOrder: ByteOrder = ByteOrder.LittleEndian) = readIntoBuffer(Long.SIZE_BYTES).readLong(byteOrder = byteOrder)

    private fun readIntoBuffer(size: Int): ByteArray {
        val buffer = ByteArray(size)
        val rc = readBytes(buffer)
        return if (rc == size) buffer else throw IOException()
    }

    fun expect(rc: Int) {
        val b = read()
        if (b != rc) throw IOException("Invalid response: expected $rc but got $b")
    }

    companion object {
        private val provider: SerialPortProvider by lazy {
            ServiceLoader.load(SerialPortProvider::class.java).first()
        }

        fun getAvailablePorts(): List<String> = provider.getAvailablePorts()
        fun getPort(name: String): SerialPort = provider.getPort(name)
    }
}

