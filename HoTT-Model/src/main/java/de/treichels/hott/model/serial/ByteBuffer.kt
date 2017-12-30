package de.treichels.hott.model.serial

import de.treichels.hott.util.Util
import java.io.IOException

class ByteBuffer(val size: Int) {
    private val DEBUG = Util.DEBUG
    private val TIMEOUT = 500
    private val monitor = java.lang.Object()

    /** The buffer holds data from [.index] inclusive to [.limit] exclusive.  */
    val buffer: ByteArray = ByteArray(size)

    /** Read position in the buffer. Any read will increment the index. The next byte to be read from is [.buffer][[.index] % [.size]]  */
    var index: Long = 0
        private set

    /** Write position in the buffer. Any write will increment the limit. The next byte to be written is [.buffer][[.limit] % [.size]]  */
    var limit: Long = 0
        private set

    /** The number of bytes available for read.  */
    fun available(): Int {
        return (limit - index).toInt()
    }

    @Throws(InterruptedException::class)
    private fun block() {
        synchronized(monitor) {
            monitor.wait(50)
        }
    }

    /** Read a single byte from the buffer. Return -1 if buffer was empty.  */
    @Synchronized
    fun read(): Int {
        // buffer underflow
        if (available() == 0) return -1

        // index in buffer
        val i = (index % size).toInt()

        // increment index
        index++

        val result = buffer[i].toInt() and 0xff

        unblock()

        return result
    }

    /** Bulk read method. Read up to data.length bytes from the buffer. Return the actual number of bytes read.  */
    @Synchronized
    fun read(data: ByteArray): Int {
        val available = available()

        // buffer underflow
        if (available == 0) return 0

        // index in buffer
        val i = (index % size).toInt()

        // increment index
        val len = Math.min(data.size, available)
        index += len.toLong()

        // space from index to end of buffer
        val x = size - i

        if (len <= x)
        // read data in one piece
            System.arraycopy(buffer, i, data, 0, len)
        else {
            // split and wrap around
            System.arraycopy(buffer, i, data, 0, x)
            System.arraycopy(buffer, 0, data, x, len - x)
        }

        unblock()

        return len
    }

    /** The number of bytes remaining for write.  */
    fun remaining(): Int {
        return size - available()
    }

    fun reset() {
        limit = 0
        index = limit
    }

    private fun unblock() {
        synchronized(monitor) {
            // if (DEBUG) System.out.println("unblock");
            monitor.notifyAll()
        }
    }

    /**
     * Block current thread until at least amount bytes become available for reading.
     *
     * @throws InterruptedException
     */

    fun waitRead(amount: Int) {
        val start = System.currentTimeMillis()
        while (available() < amount) {
            if (DEBUG) System.out.printf("Not enough data available for read, blocking: requested=%d, available=%d%n", amount, available())
            try {
                block()
            } catch (e: InterruptedException) {
                throw IOException(e)
            }

            if (System.currentTimeMillis() - start > TIMEOUT) throw IOException("read timeout")
        }
    }

    /**
     * Block current thread until at least amount bytes become available for writing.
     *
     * @throws InterruptedException
     */

    fun waitWrite(amount: Int) {
        val start = System.currentTimeMillis()
        while (remaining() < amount) {
            if (DEBUG) System.out.printf("Not enough data available for write, blocking: requested=%d, available=%d%n", amount, available())
            try {
                block()
            } catch (e: InterruptedException) {
                throw IOException(e)
            }

            if (System.currentTimeMillis() - start > TIMEOUT) throw IOException("write timeout")
        }
    }

    /** Bulk write method. Write up to data.length bytes to the buffer. Return the actual number of bytes written.  */
    @Synchronized
    fun write(data: ByteArray): Int {
        val remaining = remaining()

        // buffer overflow
        if (remaining == 0) {
            System.err.printf("buffer overflow: %d bytes were lost!%n%s%n", data.size, Util.dumpData(data))
            return 0
        }

        // limit in buffer
        val l = (limit % size).toInt()

        // increment limit
        val len = Math.min(data.size, remaining)
        limit += len.toLong()

        // space from limit to end of buffer
        val x = size - l

        if (len <= x)
        // write data in one piece
            System.arraycopy(data, 0, buffer, l, len)
        else {
            // split and wrap around
            System.arraycopy(data, 0, buffer, l, x)
            System.arraycopy(data, x, buffer, 0, len - x)
        }

        unblock()

        return len
    }

    /** Write a single byte to the buffer. Return false if buffer was full.  */
    @Synchronized
    fun write(b: Int): Boolean {
        val remaining = remaining()
        // buffer overflow
        if (remaining == 0) {
            System.err.printf("buffer overflow: %d wase lost!%n", b and 0xff)
            return false
        }

        // limit in buffer
        val l = (limit % size).toInt()

        // increment limit
        limit++

        buffer[l] = b.toByte()

        unblock()

        return true
    }
}
