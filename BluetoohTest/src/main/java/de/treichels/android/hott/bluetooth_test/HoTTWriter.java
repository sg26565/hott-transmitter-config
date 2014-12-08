package de.treichels.android.hott.bluetooth_test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Write binary data in the HoTT byte order.
 *
 * <p>
 * This class allows to write binary data in the byte order of the HoTT model files. It is similar in design to the standard {@link java.io.DataOutputStream}.
 * However, in contrast to standard Java, the bytes are stored with the least significant byte first.
 * </p>
 *
 * <p>
 * Given a 32 bit int value, the four bytes are stored in the following order:
 * <ul>
 * <li><code>b[0], b[1], b[2], b[3]</code> with HoTTWriter
 * <li><code>b[3], b[2], b[1], b[0]</code> with DataOutputSream
 * </ul>
 * </p>
 *
 * @author oli@treichels.de
 */
public class HoTTWriter {
  /** calculate CRC-16 checksum on the fly */
  private final CRC16         crc        = new CRC16();

  /** The underlying output stream */
  private final OutputStream  outputStream;

  public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1"); //$NON-NLS-1$

  /**
   * Create a HoTT writer with an internal buffer as a target.
   *
   * @param outputStream
   *          The output stream
   */
  public HoTTWriter() {
    this(new ByteArrayOutputStream());
  }

  /**
   * Create a HoTT writer with the specified {@link java.io.OutputStream} as a target.
   *
   * @param outputStream
   *          The output stream
   */
  public HoTTWriter(final OutputStream outputStream) {
    this.outputStream = outputStream;
  }

  /**
   * Get the calculated checksum. Call this method after all bytes of a data block have been read.
   *
   * @return the checksum
   */
  public int getCRC() {
    return crc.getValue();
  }

  public byte[] getData() {
    if (!(outputStream instanceof ByteArrayOutputStream)) {
      throw new IllegalStateException(outputStream.getClass().getName());
    }

    return ((ByteArrayOutputStream) outputStream).toByteArray();
  }

  /**
   * Reset the CRC-16 checksum. Call this method at the begin of a data block. The checksum will be calculated while data is read.
   */
  public void resetCRC() {
    crc.reset();
  }

  /**
   * Skip over the specified number of bytes from the underlying output steam.
   *
   * @param n
   *          the number of bytes to skip
   * @throws IOException
   *           from the underlying output stream
   */
  public void skip(final int n) throws IOException {
    int count = n;
    while (count-- > 0) {
      writeByte((byte) 0);
    }
  }

  /**
   * Write an array of signed 8 bit bytes to the underlying output stream. The specified array will be completely written to the output stream.
   *
   * @param buffer
   *          the byte array to read
   * @throws IOException
   *           from underlying output stream
   */
  public void write(final byte[] buffer) throws IOException {
    write(buffer, 0, buffer.length);
  }

  /**
   * Write an array of signed 8 bit bytes to the underlying output stream. Only the specified range will be processed.
   *
   * <p>
   * The values <code>buffer[off] .. buffer[off+len-1]</code> will be written to the output stream.
   * </p>
   *
   * @param buffer
   *          the byte array to read
   * @param off
   *          the offset into the array
   * @param len
   *          the number of bytes to write to the output stream
   * @throws IOException
   *           from underlying output stream
   */
  public void write(final byte[] buffer, final int off, final int len) throws IOException {
    outputStream.write(buffer, off, len);
    crc.update(buffer, off, len);
  }

  /**
   * Write a boolean (1 for <code>true</code> and 0 for <code>false</code>) to the underlying output stream.
   *
   * @param value
   *          the boolean
   * @throws IOException
   *           from underlying output stream
   */
  public void writeBoolean(final boolean value) throws IOException {
    writeByte((byte) (value ? 1 : 0));
  }

  /**
   * Write a signed 8 bit byte to the underlying output stream.
   *
   * @param value
   *          the byte value (-0x80 .. 0x7F)
   * @throws IOException
   *           from underlying output stream
   */
  public void writeByte(final byte value) throws IOException {
    writeUnsignedByte(value & 0xFF);
  }

  /**
   * Write a signed 32 bit int to the underlying output stream.
   *
   * @param value
   *          the int value (-0x80000000 .. 0x7FFFFFFF)
   * @throws IOException
   *           from underlying output stream
   */
  public void writeInt(final int value) throws IOException {
    writeUnsignedInt(value & 0xFFFFFFFFL);
  }

  /**
   * Write a signed 64 bit long to the underlying output stream.
   *
   * @param value
   *          the long value (-0x8000000000000000 .. 0x7FFFFFFFFFFFFFFF)
   * @throws IOException
   *           from underlying output stream
   */
  public void writeLong(final long value) throws IOException {
    writeUnsignedInt(value & 0xFFFFFFFF);
    writeUnsignedInt(value >>> Integer.SIZE);
  }

  /**
   * Write a signed 16 bit short to the underlying output stream.
   *
   * @param value
   *          the short value (-0x8000 .. 0x7FFF)
   * @throws IOException
   *           from underlying output stream
   */
  public void writeShort(final short value) throws IOException {
    writeUnsignedShort(value & 0xFFFF);
  }

  /**
   * Write a string as an array of bytes.
   *
   * @param string
   *          the string
   * @throws IOException
   *           from underlying output stream
   */
  public void writeString(final String string) throws IOException {
    write(string.getBytes(ISO_8859_1));
  }

  /**
   * Write a string as an array of bytes and fill the array with blanks.
   *
   * @param string
   *          the string
   * @throws IOException
   *           from underlying output stream
   */
  public void writeString(final String string, final int length) throws IOException {
    final byte[] array = Arrays.copyOf(string.getBytes(ISO_8859_1), length);

    for (int i = string.length(); i < length; i++) {
      array[i] = ' ';
    }

    write(array);
  }

  /**
   * Write an unsigned 8 bit byte to the underlying output stream.
   *
   * @param value
   *          the byte value (0x00 .. 0xFF)
   * @throws IOException
   *           from underlying output stream
   */
  public void writeUnsignedByte(final int value) throws IOException {
    outputStream.write(value);
    crc.update((byte) value);
  }

  /**
   * Write an unsigned 32 bit int to the underlying output stream.
   *
   * @param value
   *          the int value (0x00000000 .. 0xFFFFFFFF)
   * @throws IOException
   *           from underlying output stream
   */
  public void writeUnsignedInt(final long value) throws IOException {
    writeUnsignedShort((int) (value & 0xFFFF));
    writeUnsignedShort((int) (value >>> Short.SIZE));
  }

  /**
   * Write an unsigned 16 bit short to the underlying output stream.
   *
   * @param value
   *          the short value (0x0000 .. 0xFFFF)
   * @throws IOException
   *           from underlying output stream
   */
  public void writeUnsignedShort(final int value) throws IOException {
    writeUnsignedByte(value & 0xFF);
    writeUnsignedByte(value >>> Byte.SIZE);
  }
}
