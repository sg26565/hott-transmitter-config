package de.treichels.hott.util

import sun.reflect.annotation.AnnotationParser.toArray
import java.io.InputStream
import java.io.OutputStream

enum class ByteOrder {
    LittleEndian, BigEndian
}
// readByte
fun InputStream.readByte() = read().toByte()

@ExperimentalUnsignedTypes
fun ByteArray.readByte(offset: Int = 0) = this[offset]

@ExperimentalUnsignedTypes
fun UByteArray.readByte(offset: Int = 0) = this[offset].toByte()

// readUByte
@ExperimentalUnsignedTypes
fun InputStream.readUByte() = read().toUByte()

@ExperimentalUnsignedTypes
fun ByteArray.readUByte(offset: Int = 0) = this[offset].toUByte()

@ExperimentalUnsignedTypes
fun UByteArray.readUByte(offset: Int = 0) = this[offset]

// readShort
@ExperimentalUnsignedTypes
fun InputStream.readShort(byteOrder: ByteOrder = ByteOrder.LittleEndian) = readUShort(byteOrder).toShort()

@ExperimentalUnsignedTypes
fun ByteArray.readShort(offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = readUShort(offset, byteOrder).toShort()


@ExperimentalUnsignedTypes
fun UByteArray.readShort(offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = readUShort(offset, byteOrder).toShort()

// readUShort
@ExperimentalUnsignedTypes
fun InputStream.readUShort(byteOrder: ByteOrder = ByteOrder.LittleEndian) = when (byteOrder) {
    ByteOrder.LittleEndian -> (readUByte() + (readUByte().toUShort() shl 8)).toUShort()
    ByteOrder.BigEndian -> ((readUByte().toUShort() shl 8) + readUByte()).toUShort()
}

@ExperimentalUnsignedTypes
fun ByteArray.readUShort(offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = when (byteOrder) {
    ByteOrder.LittleEndian -> (readUByte(offset) + (readUByte(offset + 1).toUShort() shl 8)).toUShort()
    ByteOrder.BigEndian -> ((readUByte(offset).toUShort() shl 8) + readUByte(offset + 1)).toUShort()
}

@ExperimentalUnsignedTypes
fun UByteArray.readUShort(offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = when (byteOrder) {
    ByteOrder.LittleEndian -> (readUByte(offset) + (readUByte(offset + 1).toUShort() shl 8)).toUShort()
    ByteOrder.BigEndian -> ((readUByte(offset).toUShort() shl 8) + readUByte(offset + 1)).toUShort()
}

// readInt
@ExperimentalUnsignedTypes
fun InputStream.readInt(byteOrder: ByteOrder = ByteOrder.LittleEndian) = readUInt(byteOrder).toInt()

@ExperimentalUnsignedTypes
fun ByteArray.readInt(offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = readUInt(offset, byteOrder).toInt()

@ExperimentalUnsignedTypes
fun UByteArray.readInt(offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = readUInt(offset, byteOrder).toInt()

// readUInt
@ExperimentalUnsignedTypes
fun InputStream.readUInt(byteOrder: ByteOrder = ByteOrder.LittleEndian) = when (byteOrder) {
    ByteOrder.LittleEndian -> readUShort() + (readUShort().toUInt() shl 16)
    ByteOrder.BigEndian -> (readUShort().toUInt() shl 16) + readUShort()
}

@ExperimentalUnsignedTypes
fun ByteArray.readUInt(offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = when (byteOrder) {
    ByteOrder.LittleEndian -> readUShort(offset) + (readUShort(offset + 2).toUInt() shl 16)
    ByteOrder.BigEndian -> (readUShort(offset).toUInt() shl 16) + readUShort(offset + 2)
}

@ExperimentalUnsignedTypes
fun UByteArray.readUInt(offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = when (byteOrder) {
    ByteOrder.LittleEndian -> readUShort(offset) + (readUShort(offset + 2).toUInt() shl 16)
    ByteOrder.BigEndian -> (readUShort(offset).toUInt() shl 16) + readUShort(offset + 2)
}

// readLong
@ExperimentalUnsignedTypes
fun InputStream.readLong(byteOrder: ByteOrder = ByteOrder.LittleEndian) = readULong(byteOrder).toLong()

@ExperimentalUnsignedTypes
fun ByteArray.readLong(offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = readULong(offset, byteOrder).toLong()

@ExperimentalUnsignedTypes
fun UByteArray.readLong(offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = readULong(offset, byteOrder).toLong()

// readULong
@ExperimentalUnsignedTypes
fun InputStream.readULong(byteOrder: ByteOrder = ByteOrder.LittleEndian) = when (byteOrder) {
    ByteOrder.LittleEndian -> readUInt() + (readUInt().toULong() shl 32)
    ByteOrder.BigEndian -> (readUInt().toULong() shl 32) + readUInt()
}

@ExperimentalUnsignedTypes
fun ByteArray.readULong(offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = when (byteOrder) {
    ByteOrder.LittleEndian -> readUInt(offset) + (readUInt(offset + 4).toULong() shl 32)
    ByteOrder.BigEndian -> (readUInt(offset).toULong() shl 32) + readUInt(offset + 4)
}

@ExperimentalUnsignedTypes
fun UByteArray.readULong(offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = when (byteOrder) {
    ByteOrder.LittleEndian -> readUInt(offset) + (readUInt(offset + 4).toULong() shl 32)
    ByteOrder.BigEndian -> (readUInt(offset).toULong() shl 32) + readUInt(offset + 4)
}

// writeByte
@ExperimentalUnsignedTypes
fun OutputStream.writeByte(byte: Byte) = writeUByte(byte.toUByte())

fun ByteArray.writeByte(byte: Byte, offset: Int = 0) {
    this[offset] = byte
}

@ExperimentalUnsignedTypes
fun UByteArray.writeByte(byte: Byte, offset: Int = 0) {
    this[offset] = byte.toUByte()
}

// writeUByte
@ExperimentalUnsignedTypes
fun OutputStream.writeUByte(ubyte: UByte) = write(ubyte.toInt())

@ExperimentalUnsignedTypes
fun ByteArray.writeUByte(ubyte: UByte, offset: Int = 0) {
    this[offset] = ubyte.toByte()
}

@ExperimentalUnsignedTypes
fun UByteArray.writeUByte(ubyte: UByte, offset: Int = 0) {
    this[offset] = ubyte
}

// writeShort
@ExperimentalUnsignedTypes
fun OutputStream.writeShort(short: Short, byteOrder: ByteOrder = ByteOrder.LittleEndian) = writeUShort(short.toUShort(), byteOrder)

@ExperimentalUnsignedTypes
fun ByteArray.writeShort(short: Short, offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = writeUShort(short.toUShort(), offset, byteOrder)

@ExperimentalUnsignedTypes
fun UByteArray.writeShort(short: Short, offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = writeUShort(short.toUShort(), offset, byteOrder)

// writeUShort
@ExperimentalUnsignedTypes
fun OutputStream.writeUShort(short: UShort, byteOrder: ByteOrder = ByteOrder.LittleEndian) {
    when (byteOrder) {
        ByteOrder.LittleEndian -> {
            writeUByte(short.toUByte())
            writeUByte((short shr 8).toUByte())
        }
        ByteOrder.BigEndian -> {
            writeUByte((short shr 8).toUByte())
            writeUByte(short.toUByte())
        }
    }
}

@ExperimentalUnsignedTypes
fun ByteArray.writeUShort(ushort: UShort, offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) {
    when (byteOrder) {
        ByteOrder.LittleEndian -> {
            writeUByte(ushort.toUByte(), offset)
            writeUByte((ushort shr 8).toUByte(), offset + 1)
        }
        ByteOrder.BigEndian -> {
            writeUByte((ushort shr 8).toUByte(), offset)
            writeUByte(ushort.toUByte(), offset + 1)
        }
    }
}

@ExperimentalUnsignedTypes
fun UByteArray.writeUShort(ushort: UShort, offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) {
    when (byteOrder) {
        ByteOrder.LittleEndian -> {
            writeUByte(ushort.toUByte(), offset)
            writeUByte((ushort shr 8).toUByte(), offset + 1)
        }
        ByteOrder.BigEndian -> {
            writeUByte((ushort shr 8).toUByte(), offset)
            writeUByte(ushort.toUByte(), offset + 1)
        }
    }
}

// writeInt
@ExperimentalUnsignedTypes
fun OutputStream.writeInt(int: Int, byteOrder: ByteOrder = ByteOrder.LittleEndian) = writeUInt(int.toUInt(), byteOrder)

@ExperimentalUnsignedTypes
fun ByteArray.writeInt(int: Int, offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = writeUInt(int.toUInt(), offset, byteOrder)

@ExperimentalUnsignedTypes
fun UByteArray.writeInt(int: Int, offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = writeUInt(int.toUInt(), offset, byteOrder)

// writeUInt
@ExperimentalUnsignedTypes
fun OutputStream.writeUInt(uint: UInt, byteOrder: ByteOrder = ByteOrder.LittleEndian) {
    when (byteOrder) {
        ByteOrder.LittleEndian -> {
            writeUShort(uint.toUShort())
            writeUShort((uint shr 16).toUShort())
        }
        ByteOrder.BigEndian -> {
            writeUShort((uint shr 16).toUShort())
            writeUShort(uint.toUShort())
        }
    }
}

@ExperimentalUnsignedTypes
fun ByteArray.writeUInt(uint: UInt, offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) {
    when (byteOrder) {
        ByteOrder.LittleEndian -> {
            writeUShort(uint.toUShort(), offset)
            writeUShort((uint shr 16).toUShort(), offset + 2)
        }
        ByteOrder.BigEndian -> {
            writeUShort((uint shr 16).toUShort(), offset)
            writeUShort(uint.toUShort(), offset + 2)
        }
    }
}

@ExperimentalUnsignedTypes
fun UByteArray.writeUInt(uint: UInt, offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) {
    when (byteOrder) {
        ByteOrder.LittleEndian -> {
            writeUShort(uint.toUShort(), offset)
            writeUShort((uint shr 16).toUShort(), offset + 2)
        }
        ByteOrder.BigEndian -> {
            writeUShort((uint shr 16).toUShort(), offset)
            writeUShort(uint.toUShort(), offset + 2)
        }
    }
}

// writeLong
@ExperimentalUnsignedTypes
fun OutputStream.writeLong(long: Long, byteOrder: ByteOrder = ByteOrder.LittleEndian) = writeULong(long.toULong(), byteOrder)

@ExperimentalUnsignedTypes
fun ByteArray.writeLong(long: Long, offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = writeULong(long.toULong(), offset, byteOrder)

@ExperimentalUnsignedTypes
fun UByteArray.writeLong(long: Long, offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) = writeULong(long.toULong(), offset, byteOrder)

// writeULong
@ExperimentalUnsignedTypes
fun OutputStream.writeULong(ulong: ULong, byteOrder: ByteOrder = ByteOrder.LittleEndian) {
    when (byteOrder) {
        ByteOrder.LittleEndian -> {
            writeUInt(ulong.toUInt())
            writeUInt((ulong shr 32).toUInt())
        }
        ByteOrder.BigEndian -> {
            writeUInt((ulong shr 32).toUInt())
            writeUInt(ulong.toUInt())
        }
    }
}

@ExperimentalUnsignedTypes
fun ByteArray.writeULong(ulong: ULong, offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) {
    when (byteOrder) {
        ByteOrder.LittleEndian -> {
            writeUInt(ulong.toUInt(), offset)
            writeUInt((ulong shr 32).toUInt(), offset + 4)
        }
        ByteOrder.BigEndian -> {
            writeUInt((ulong shr 32).toUInt(), offset)
            writeUInt(ulong.toUInt(), offset + 4)
        }
    }
}

@ExperimentalUnsignedTypes
fun UByteArray.writeULong(ulong: ULong, offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LittleEndian) {
    when (byteOrder) {
        ByteOrder.LittleEndian -> {
            writeUInt(ulong.toUInt(), offset)
            writeUInt((ulong shr 32).toUInt(), offset + 4)
        }
        ByteOrder.BigEndian -> {
            writeUInt((ulong shr 32).toUInt(), offset)
            writeUInt(ulong.toUInt(), offset + 4)
        }
    }
}

@ExperimentalUnsignedTypes
infix fun UShort.shl(bits: Int) = this.toUInt().shl(bits).toUShort()

@ExperimentalUnsignedTypes
infix fun UShort.shr(bits: Int) = this.toUInt().shr(bits).toUShort()
