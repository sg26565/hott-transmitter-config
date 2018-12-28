package de.treichels.hott.util

import org.junit.Test

import org.junit.Assert.*
import java.io.ByteArrayOutputStream
import java.lang.IllegalArgumentException

class StreamSupportKtTest {
    val data = ByteArray(256) {
        it.toByte()
    }

    @Test
    fun testReadByte() {
        data.inputStream().use {
            for (i in 0..255) {
                assertEquals(i.toByte(), it.readByte())
            }
        }

        byteArrayOf(-2, -1, 0, 1, 2).inputStream().use {
            assertEquals((-2).toByte(), it.readByte())
            assertEquals((-1).toByte(), it.readByte())
            assertEquals(0.toByte(), it.readByte())
            assertEquals(1.toByte(), it.readByte())
            assertEquals(2.toByte(), it.readByte())
        }
    }

    @Test
    fun testReadUnsignedByte() {
        data.inputStream().use {
            for (i in 0..255) {
                assertEquals(i, it.readUnsignedByte())
            }
        }

        byteArrayOf(-2, -1, 0, 1, 2).inputStream().use {
            assertEquals(254, it.readUnsignedByte())
            assertEquals(255, it.readUnsignedByte())
            assertEquals(0, it.readUnsignedByte())
            assertEquals(1, it.readUnsignedByte())
            assertEquals(2, it.readUnsignedByte())
        }
    }

    @Test
    fun testReadShort() {
        byteArrayOf(-2, -1, -1, -1, 0, 0, 1, 0, 2, 0, 0, 1, 0, 4).inputStream().use {
            assertEquals((-2).toShort(), it.readShort())
            assertEquals((-1).toShort(), it.readShort())
            assertEquals(0.toShort(), it.readShort())
            assertEquals(1.toShort(), it.readShort())
            assertEquals(2.toShort(), it.readShort())
            assertEquals(256.toShort(), it.readShort())
            assertEquals(1024.toShort(), it.readShort())
        }
    }

    @Test
    fun testReadUnsignedShort() {
        byteArrayOf(-2, -1, -1, -1, 0, 0, 1, 0, 2, 0, 0, 1, 0, 4).inputStream().use {
            assertEquals(65534, it.readUnsignedShort())
            assertEquals(65535, it.readUnsignedShort())
            assertEquals(0, it.readUnsignedShort())
            assertEquals(1, it.readUnsignedShort())
            assertEquals(2, it.readUnsignedShort())
            assertEquals(256, it.readUnsignedShort())
            assertEquals(1024, it.readUnsignedShort())
        }
    }

    @Test
    fun testReadUnsignedInt() {
        byteArrayOf(-2, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0).inputStream().use {
            assertEquals(0xfffffffe, it.readUnsignedInt())
            assertEquals(0xffffffff, it.readUnsignedInt())
            assertEquals(0L, it.readUnsignedInt())
            assertEquals(1L, it.readUnsignedInt())
            assertEquals(2L, it.readUnsignedInt())
            assertEquals(65536L, it.readUnsignedInt())
        }
    }

    @Test
    fun testReadInt() {
        byteArrayOf(-2, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0).inputStream().use {
            assertEquals(-2, it.readInt())
            assertEquals(-1, it.readInt())
            assertEquals(0, it.readInt())
            assertEquals(1, it.readInt())
            assertEquals(2, it.readInt())
            assertEquals(65536, it.readInt())
        }
    }

    @Test
    fun testReadLong() {
        byteArrayOf(-2, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0).inputStream().use {
            assertEquals(-2, it.readLong())
            assertEquals(0x0000000100000000, it.readLong())
            assertEquals(0x0001000000000002, it.readLong())
        }
    }

    @Test
    fun testWriteByte() {
        val expected = byteArrayOf(-2, -1, 0, 1, 2)
        val data = ByteArrayOutputStream().apply {
            writeByte(-2)
            writeByte(-1)
            writeByte(0)
            writeByte(1)
            writeByte(2)
        }

        assertArrayEquals(expected, data.toByteArray())
    }

    @Test
    fun testWriteUnsignedByte() {
        val expected = byteArrayOf(-2, -1, 0, 1, 2)
        val data = ByteArrayOutputStream().apply {
            writeUnsignedByte(254)
            writeUnsignedByte(255)
            writeUnsignedByte(0)
            writeUnsignedByte(1)
            writeUnsignedByte(2)
        }

        assertArrayEquals(expected, data.toByteArray())
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidWriteUnsignedByte() {
        ByteArrayOutputStream().writeUnsignedByte(-1)
    }

    @Test
    fun testWriteShort() {
        val expected = byteArrayOf(-2, -1, -1, -1, 0, 0, 1, 0, 2, 0, 0, 1, 0, 4)
        val data = ByteArrayOutputStream().apply {
            writeShort(-2)
            writeShort(-1)
            writeShort(0)
            writeShort(1)
            writeShort(2)
            writeShort(256)
            writeShort(1024)
        }

        assertArrayEquals(expected, data.toByteArray())
    }

    @Test
    fun testWriteUnsignedShort() {
        val expected = byteArrayOf(-2, -1, -1, -1, 0, 0, 1, 0, 2, 0, 0, 1, 0, 4)
        val data = ByteArrayOutputStream().apply {
            writeUnsignedShort(65534)
            writeUnsignedShort(65535)
            writeUnsignedShort(0)
            writeUnsignedShort(1)
            writeUnsignedShort(2)
            writeUnsignedShort(256)
            writeUnsignedShort(1024)
        }

        assertArrayEquals(expected, data.toByteArray())
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidWriteUnsignedShort() {
        ByteArrayOutputStream().writeUnsignedShort(-1)
    }

    @Test
    fun testWriteInt() {
        val expected = byteArrayOf(-2, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0)
        val data = ByteArrayOutputStream().apply {
            writeInt(-2)
            writeInt(-1)
            writeInt(0)
            writeInt(1)
            writeInt(2)
            writeInt(65536)
        }

        assertArrayEquals(expected, data.toByteArray())
    }

    @Test
    fun testWriteUnsignedInt() {
        val expected = byteArrayOf(-2, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0)
        val data = ByteArrayOutputStream().apply {
            writeUnsignedInt(0xfffffffe)
            writeUnsignedInt(0xffffffff)
            writeUnsignedInt(0)
            writeUnsignedInt(1)
            writeUnsignedInt(2)
            writeUnsignedInt(65536)
        }

        assertArrayEquals(expected, data.toByteArray())
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidWriteUnsignedInt() {
        ByteArrayOutputStream().writeUnsignedInt(-1)
    }

    @Test
    fun testWriteLong() {
        val expected = byteArrayOf(-2, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0)
        val data = ByteArrayOutputStream().apply {
            writeLong(-2L)
            writeLong(0x0000000100000000)
            writeLong(0x0001000000000002)
        }

        assertArrayEquals(expected, data.toByteArray())
    }
}
