package de.treichels.hott.model.serial

import org.junit.Test

import org.junit.Assert.*

class ByteBufferTest {
    @Test
    fun testBulkReadWrite() {
        val b = ByteBuffer(5)
        val data = ByteArray(3)

        assertEquals(5, b.write(byteArrayOf(0, 1, 2, 3, 4)).toLong())
        assertEquals(5, b.available.toLong())
        assertEquals(0, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(0, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        assertEquals(0, b.write(byteArrayOf(5, 6, 7)).toLong()) // buffer overflow
        assertEquals(5, b.available.toLong())
        assertEquals(0, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(0, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        assertEquals(3, b.read(data).toLong())
        assertArrayEquals(byteArrayOf(0, 1, 2), data)
        assertEquals(2, b.available.toLong())
        assertEquals(3, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(3, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        assertEquals(2, b.read(data).toLong()) // buffer underflow
        assertArrayEquals(byteArrayOf(3, 4, 2), data)
        assertEquals(0, b.available.toLong())
        assertEquals(5, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(5, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        assertEquals(4, b.write(byteArrayOf(5, 6, 7, 8)).toLong())
        assertEquals(4, b.available.toLong())
        assertEquals(5, b.index)
        assertEquals(9, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(1, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(5, 6, 7, 8, 4), b.buffer)

        assertEquals(3, b.read(data).toLong())
        assertArrayEquals(byteArrayOf(5, 6, 7), data)
        assertEquals(1, b.available.toLong())
        assertEquals(8, b.index)
        assertEquals(9, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(4, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(5, 6, 7, 8, 4), b.buffer)

        assertEquals(4, b.write(byteArrayOf(9, 10, 11, 12, 13, 14, 15)).toLong())
        assertEquals(5, b.available.toLong())
        assertEquals(8, b.index)
        assertEquals(13, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(0, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(10, 11, 12, 8, 9), b.buffer)

        assertEquals(3, b.read(data).toLong())
        assertArrayEquals(byteArrayOf(8, 9, 10), data)
        assertEquals(2, b.available.toLong())
        assertEquals(11, b.index)
        assertEquals(13, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(3, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(10, 11, 12, 8, 9), b.buffer)

        assertEquals(2, b.read(data).toLong()) // buffer underflow
        assertArrayEquals(byteArrayOf(11, 12, 10), data)
        assertEquals(0, b.available.toLong())
        assertEquals(13, b.index)
        assertEquals(13, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(5, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(10, 11, 12, 8, 9), b.buffer)
    }

    @Test
    fun testByteBuffer() {
        val b = ByteBuffer(5)

        assertEquals(0, b.available.toLong())
        assertEquals(0, b.index)
        assertEquals(0, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(5, b.remaining.toLong())
        assertEquals(-1, b.read().toLong())
        assertArrayEquals(byteArrayOf(0, 0, 0, 0, 0), b.buffer)
    }

    @Test
    fun testReadWriteByte() {
        val b = ByteBuffer(5)

        // fill buffer (0 .. 4)
        for (i in 0..4) {
            assertEquals(i.toLong(), b.available.toLong())
            assertEquals(0, b.index)
            assertEquals(i.toLong(), b.limit)
            assertEquals(5, b.size.toLong())
            assertEquals((5 - i).toLong(), b.remaining.toLong())
            assertTrue(b.write(i.toInt()))
        }

        // check if full
        assertFalse(b.write(5.toByte().toInt())) // buffer overflow
        assertEquals(5, b.available.toLong())
        assertEquals(0, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(0, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        // read one byte (0)
        assertEquals(0, b.read().toLong())
        assertEquals(4, b.available.toLong())
        assertEquals(1, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(1, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        // read one byte (1)
        assertEquals(1, b.read().toLong())
        assertEquals(3, b.available.toLong())
        assertEquals(2, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(2, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        // read one byte (2)
        assertEquals(2, b.read().toLong())
        assertEquals(2, b.available.toLong())
        assertEquals(3, b.index)
        assertEquals(5, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(3, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(0, 1, 2, 3, 4), b.buffer)

        // write one byte (5)
        assertTrue(b.write(5.toByte().toInt()))
        assertEquals(3, b.available.toLong())
        assertEquals(3, b.index)
        assertEquals(6, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(2, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(5, 1, 2, 3, 4), b.buffer)

        // write one byte (6)
        assertTrue(b.write(6.toByte().toInt()))
        assertEquals(4, b.available.toLong())
        assertEquals(3, b.index)
        assertEquals(7, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(1, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(5, 6, 2, 3, 4), b.buffer)

        // read one byte (3)
        assertEquals(3, b.read().toLong())
        assertEquals(3, b.available.toLong())
        assertEquals(4, b.index)
        assertEquals(7, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(2, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(5, 6, 2, 3, 4), b.buffer)

        // read one byte (4)
        assertEquals(4, b.read().toLong())
        assertEquals(2, b.available.toLong())
        assertEquals(5, b.index)
        assertEquals(7, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(3, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(5, 6, 2, 3, 4), b.buffer)

        // read one byte (5)
        assertEquals(5, b.read().toLong())
        assertEquals(1, b.available.toLong())
        assertEquals(6, b.index)
        assertEquals(7, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(4, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(5, 6, 2, 3, 4), b.buffer)

        // read one byte (6)
        assertEquals(6, b.read().toLong())
        assertEquals(0, b.available.toLong())
        assertEquals(7, b.index)
        assertEquals(7, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(5, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(5, 6, 2, 3, 4), b.buffer)

        // read one byte - buffer underflow
        assertEquals(-1, b.read().toLong())
        assertEquals(0, b.available.toLong())
        assertEquals(7, b.index)
        assertEquals(7, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(5, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(5, 6, 2, 3, 4), b.buffer)

        // write one byte (7)
        assertTrue(b.write(7.toByte().toInt()))
        assertEquals(1, b.available.toLong())
        assertEquals(7, b.index)
        assertEquals(8, b.limit)
        assertEquals(5, b.size.toLong())
        assertEquals(4, b.remaining.toLong())
        assertArrayEquals(byteArrayOf(5, 6, 7, 3, 4), b.buffer)
    }
}
