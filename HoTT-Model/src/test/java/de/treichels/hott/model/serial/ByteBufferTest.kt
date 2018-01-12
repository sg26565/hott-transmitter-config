package de.treichels.hott.model.serial

import org.junit.Assert.*
import org.junit.Test

class ByteBufferTest {
    @Test
    fun testBulkReadWrite() {
        val b = ByteBuffer(5)
        val data = ByteArray(3)

        assertEquals(5, b.write(byteArrayOf(0, 1, 2, 3, 4)))
        assertEquals(5, b.available)
        assertEquals(0, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size)
        assertEquals(0, b.remaining)
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        assertEquals(0, b.write(byteArrayOf(5, 6, 7))) // buffer overflow
        assertEquals(5, b.available)
        assertEquals(0, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size)
        assertEquals(0, b.remaining)
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        assertEquals(3, b.read(data))
        assertArrayEquals(byteArrayOf(0, 1, 2), data)
        assertEquals(2, b.available)
        assertEquals(3, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size)
        assertEquals(3, b.remaining)
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        assertEquals(2, b.read(data)) // buffer underflow
        assertArrayEquals(byteArrayOf(3, 4, 2), data)
        assertEquals(0, b.available)
        assertEquals(5, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size)
        assertEquals(5, b.remaining)
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        assertEquals(4, b.write(byteArrayOf(5, 6, 7, 8)))
        assertEquals(4, b.available)
        assertEquals(5, b.index)
        assertEquals(9, b.limit)
        assertEquals(5, b.size)
        assertEquals(1, b.remaining)
        assertArrayEquals(byteArrayOf(5, 6, 7, 8, 4), b.buffer)

        assertEquals(3, b.read(data))
        assertArrayEquals(byteArrayOf(5, 6, 7), data)
        assertEquals(1, b.available)
        assertEquals(8, b.index)
        assertEquals(9, b.limit)
        assertEquals(5, b.size)
        assertEquals(4, b.remaining)
        assertArrayEquals(byteArrayOf(5, 6, 7, 8, 4), b.buffer)

        assertEquals(4, b.write(byteArrayOf(9, 10, 11, 12, 13, 14, 15)))
        assertEquals(5, b.available)
        assertEquals(8, b.index)
        assertEquals(13, b.limit)
        assertEquals(5, b.size)
        assertEquals(0, b.remaining)
        assertArrayEquals(byteArrayOf(10, 11, 12, 8, 9), b.buffer)

        assertEquals(3, b.read(data))
        assertArrayEquals(byteArrayOf(8, 9, 10), data)
        assertEquals(2, b.available)
        assertEquals(11, b.index)
        assertEquals(13, b.limit)
        assertEquals(5, b.size)
        assertEquals(3, b.remaining)
        assertArrayEquals(byteArrayOf(10, 11, 12, 8, 9), b.buffer)

        assertEquals(2, b.read(data)) // buffer underflow
        assertArrayEquals(byteArrayOf(11, 12, 10), data)
        assertEquals(0, b.available)
        assertEquals(13, b.index)
        assertEquals(13, b.limit)
        assertEquals(5, b.size)
        assertEquals(5, b.remaining)
        assertArrayEquals(byteArrayOf(10, 11, 12, 8, 9), b.buffer)
    }

    @Test
    fun testByteBuffer() {
        val b = ByteBuffer(5)

        assertEquals(0, b.available)
        assertEquals(0, b.index)
        assertEquals(0, b.limit)
        assertEquals(5, b.size)
        assertEquals(5, b.remaining)
        assertEquals(-1, b.read())
        assertArrayEquals(byteArrayOf(0, 0, 0, 0, 0), b.buffer)
    }

    @Test
    fun testReadWriteByte() {
        val b = ByteBuffer(5)

        // fill buffer (0 .. 4)
        for (i in 0..4) {
            assertEquals(i, b.available)
            assertEquals(0, b.index)
            assertEquals(i, b.limit.toInt())
            assertEquals(5, b.size)
            assertEquals((5 - i), b.remaining)
            assertTrue(b.write(i))
        }

        // check if full
        assertFalse(b.write(5.toByte().toInt())) // buffer overflow
        assertEquals(5, b.available)
        assertEquals(0, b.index)
        assertEquals(5, b.limit.toInt())
        assertEquals(5, b.size)
        assertEquals(0, b.remaining)
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        // read one byte (0)
        assertEquals(0, b.read())
        assertEquals(4, b.available)
        assertEquals(1, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size)
        assertEquals(1, b.remaining)
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        // read one byte (1)
        assertEquals(1, b.read())
        assertEquals(3, b.available)
        assertEquals(2, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size)
        assertEquals(2, b.remaining)
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        // read one byte (2)
        assertEquals(2, b.read())
        assertEquals(2, b.available)
        assertEquals(3, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size)
        assertEquals(3, b.remaining)
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        // write one byte (5)
        assertTrue(b.write(5.toByte().toInt()))
        assertEquals(3, b.available)
        assertEquals(3, b.index)
        assertEquals(6, b.limit)
        assertEquals(5, b.size)
        assertEquals(2, b.remaining)
        assertArrayEquals(byteArrayOf(5, 1, 2, 3, 4), b.buffer)

        // write one byte (6)
        assertTrue(b.write(6.toByte().toInt()))
        assertEquals(4, b.available)
        assertEquals(3, b.index)
        assertEquals(7, b.limit)
        assertEquals(5, b.size)
        assertEquals(1, b.remaining)
        assertArrayEquals(byteArrayOf(5, 6, 2, 3, 4), b.buffer)

        // read one byte (3)
        assertEquals(3, b.read())
        assertEquals(3, b.available)
        assertEquals(4, b.index)
        assertEquals(7, b.limit)
        assertEquals(5, b.size)
        assertEquals(2, b.remaining)
        assertArrayEquals(byteArrayOf(5, 6, 2, 3, 4), b.buffer)

        // read one byte (4)
        assertEquals(4, b.read())
        assertEquals(2, b.available)
        assertEquals(5, b.index)
        assertEquals(7, b.limit)
        assertEquals(5, b.size)
        assertEquals(3, b.remaining)
        assertArrayEquals(byteArrayOf(5, 6, 2, 3, 4), b.buffer)

        // read one byte (5)
        assertEquals(5, b.read())
        assertEquals(1, b.available)
        assertEquals(6, b.index)
        assertEquals(7, b.limit)
        assertEquals(5, b.size)
        assertEquals(4, b.remaining)
        assertArrayEquals(byteArrayOf(5, 6, 2, 3, 4), b.buffer)

        // read one byte (6)
        assertEquals(6, b.read())
        assertEquals(0, b.available)
        assertEquals(7, b.index)
        assertEquals(7, b.limit)
        assertEquals(5, b.size)
        assertEquals(5, b.remaining)
        assertArrayEquals(byteArrayOf(5, 6, 2, 3, 4), b.buffer)

        // read one byte - buffer underflow
        assertEquals(-1, b.read())
        assertEquals(0, b.available)
        assertEquals(7, b.index)
        assertEquals(7, b.limit)
        assertEquals(5, b.size)
        assertEquals(5, b.remaining)
        assertArrayEquals(byteArrayOf(5, 6, 2, 3, 4), b.buffer)

        // write one byte (7)
        assertTrue(b.write(7.toByte().toInt()))
        assertEquals(1, b.available)
        assertEquals(7, b.index)
        assertEquals(8, b.limit)
        assertEquals(5, b.size)
        assertEquals(4, b.remaining)
        assertArrayEquals(byteArrayOf(5, 6, 7, 3, 4), b.buffer)
    }
}
