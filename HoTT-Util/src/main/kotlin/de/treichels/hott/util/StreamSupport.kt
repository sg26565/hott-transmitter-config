package de.treichels.hott.util

import java.io.InputStream
import java.io.OutputStream

fun InputStream.readByte() = readUnsignedByte().toByte()
fun InputStream.readUnsignedByte() = read()
fun InputStream.readShort() = readUnsignedShort().toShort()
fun InputStream.readUnsignedShort() = readUnsignedByte() + (readUnsignedByte() shl 8)
fun InputStream.readInt() = readUnsignedInt().toInt()
fun InputStream.readUnsignedInt() = readUnsignedShort() + (readUnsignedShort().toLong() shl 16)
fun InputStream.readLong() = readUnsignedInt() + (readUnsignedInt() shl 32)

fun OutputStream.writeByte(b: Byte) = writeUnsignedByte(b.toInt() and 0xff)
fun OutputStream.writeUnsignedByte(b: Int) {
    if (b < 0) throw IllegalArgumentException(b.toString())
    write(b)
}

fun OutputStream.writeShort(s: Short) = writeUnsignedShort(s.toInt() and 0xffff)
fun OutputStream.writeUnsignedShort(s: Int) {
    if (s < 0) throw IllegalArgumentException(s.toString())
    writeUnsignedByte(s and 0xff)
    writeUnsignedByte(s ushr 8)
}

fun OutputStream.writeInt(i: Int) = writeUnsignedInt(i.toLong() and 0xffffffff)
fun OutputStream.writeUnsignedInt(s: Long) {
    if (s < 0) throw IllegalArgumentException(s.toString())
    writeUnsignedShort((s and 0xffff).toInt())
    writeUnsignedShort((s ushr 16).toInt())
}

fun OutputStream.writeLong(l: Long) {
    writeUnsignedInt(l and 0xffffffff)
    writeUnsignedInt(l ushr 32)
}
