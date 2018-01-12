package de.treichels.hott.model.serial

import java.util.concurrent.locks.ReentrantLock

class ByteBuffer(val size: Int) {
    companion object {
        private val DEBUG = true //java.lang.Boolean.getBoolean("ByteBuffer.debug")
    }

    /** Thread synchronization lock */
    private val lock = ReentrantLock()

    /** Condition that signals that some space is remaining for write */
    private val notFull = lock.newCondition()

    /** Condition that signals that some data is available for read */
    private val notEmpty = lock.newCondition()

    /** The buffer holds data from [.index] inclusive to [.limit] exclusive.  */
    val buffer: ByteArray = ByteArray(size)

    /** Read position in the buffer. Any read will increment the index. The next byte to be read from is [.buffer][[.index] % [.size]]  */
    var index: Long = 0
        private set

    /** Write position in the buffer. Any write will increment the limit. The next byte to be written is [.buffer][[.limit] % [.size]]  */
    var limit: Long = 0
        private set

    /** The number of bytes available for read.  */
    val available
        get() = (limit - index).toInt()

    /** The number of bytes remaining for write.  */
    val remaining
        get() = size - available

    /** Read a single byte from the buffer. Return -1 if buffer was empty.  */
    fun read(): Int {
        lock.lock()
        try {
            waitRead(1)
            // buffer underflow
            if (available == 0) return -1

            // index in buffer
            val i = (index % size).toInt()

            // increment index
            index++

            return buffer[i].toInt() and 0xff
        } finally {
            // space is available for write
            notFull.signal()
            lock.unlock()
        }
    }

    /** Bulk read method. Read up to data.length bytes from the buffer. Return the actual number of bytes read.  */
    fun read(data: ByteArray): Int {
        lock.lock()
        try {
            // buffer underflow
            if (available == 0) return 0

            // index in buffer
            val i = (index % size).toInt()

            // increment index
            val len = Math.min(data.size, available)
            index += len.toLong()

            // space from index to end of buffer
            val x = size - i
            if (len <= x) {
                // read data in one piece
                System.arraycopy(buffer, i, data, 0, len)
            } else {
                // split and wrap around
                System.arraycopy(buffer, i, data, 0, x)
                System.arraycopy(buffer, 0, data, x, len - x)
            }

            return len
        } finally {
            // space is available for write
            notFull.signal()
            lock.unlock()
        }
    }

    /** Reset this buffer */
    fun reset() {
        lock.lock()
        try {
            limit = 0
            index = 0
        } finally {
            // space is available for write
            notFull.signal()
            // data is available for read
            notEmpty.signal()
            lock.unlock()
        }
    }

    /** Block current thread until at least amount bytes become available for reading. */
    private fun waitRead(amount: Int) {
        while (available < amount) {
            if (DEBUG) println("Not enough data available for read, blocking: requested=$amount, available=$available")
            // wait until data becomes available for read
            notEmpty.await()
        }
    }

    /** Block current thread until at least amount bytes become available for writing. */
    private fun waitWrite(amount: Int) {
        while (remaining < amount) {
            if (DEBUG) println("Not enough data available for write, blocking: requested=$amount, remaining=$remaining")
            // wait until space becomes available for write
            notFull.await()
        }
    }

    /** Bulk write method. Write up to data.length bytes to the buffer. Return the actual number of bytes written.  */
    fun write(data: ByteArray): Int {
        lock.lock()
        try {
            waitWrite(data.size)

            // increment limit
            val len = Math.min(data.size, remaining)
            if (len > 0) {
                // limit in buffer
                val l = (limit % size).toInt()

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
            }

            return len
        } finally {
            // data is available for read
            notEmpty.signal()
            lock.unlock()
        }
    }

    /** Write a single byte to the buffer. Return false if buffer was full.  */
    fun write(b: Int): Boolean {
        lock.lock()
        try {
            waitWrite(1)

            // limit in buffer
            val l = (limit % size).toInt()

            // increment limit
            limit++

            buffer[l] = b.toByte()
            return true
        } finally {
            // data is available for read
            notEmpty.signal()
            lock.unlock()
        }
    }
}
