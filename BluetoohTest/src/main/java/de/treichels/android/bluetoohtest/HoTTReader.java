package de.treichels.android.bluetoohtest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Read binary data in the HoTT byte order.
 *
 * <p>
 * This class allows to read binary data in the byte order of the HoTT model files. It is similar in design to the standard {@link java.io.DataInputStream}.
 * However, in contrast to standard Java, the bytes are stored with the least significant byte first.
 * </p>
 *
 * <p>
 * Given a 32 bit int value, the four bytes are read in the following order:
 * <ul>
 * <li><code>b[0], b[1], b[2], b[3]</code> with HoTTReader
 * <li><code>b[3], b[2], b[1], b[0]</code> with DataInputSream
 * </ul>
 * </p>
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class HoTTReader {
  /** calculate CRC-16 checksum on the fly */
  private final CRC16         crc        = new CRC16();

  /** The underlying input stream */
  private final InputStream   inputStream;

  /** bytes read so far */
  private int                 offset;

  public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1"); //$NON-NLS-1$

  /**
   * Create a HoTT reader with the specified byte array as a source.
   *
   * @param inputStream
   *          The input stream
   */
  public HoTTReader(final byte[] data) {
    this(new ByteArrayInputStream(data));
  }

  /**
   * Create a HoTT reader with the specified {@link java.io.InputStream} as a source.
   *
   * @param inputStream
   *          The input stream
   */
  public HoTTReader(final InputStream inputStream) {
    this.inputStream = inputStream;
  }

  /**
   * Get the calculated checksum. Call this method after all bytes of a data block have been read.
   *
   * @return the checksum
   */
  public int getCRC() {
    return crc.getValue();
  }

  /**
   * Get the current offset into the file (number of bytes read).
   *
   * @return the offset
   */
  public int getOffset() {
    return offset;
  }

  /**
   * Read an array of signed 8 bit bytes from the underlying input stream. The specified array will be completely filled.
   *
   * @param buffer
   *          the byte array to fill
   * @throws IOException
   *           from underlying input stream
   */
  public void read(final byte[] buffer) throws IOException {
    read(buffer, 0, buffer.length);
  }

  /**
   * Read an array of signed 8 bit bytes from the underlying input stream. Only the specified range will be processed.
   *
   * <p>
   * The values <code>buffer[off] .. buffer[off+len-1]</code> will be read from the input stream.
   * </p>
   *
   * @param buffer
   *          the byte array to fill
   * @param off
   *          the offset into the array
   * @param len
   *          the number of bytes to read from the input stream
   * @throws IOException
   *           from underlying input stream
   */
  public void read(final byte[] buffer, final int off, final int len) throws IOException {
    // inputSream.read(buffer, off, len);
    // crc.update(buffer, off, len);
    if (buffer == null) {
      throw new NullPointerException();
    } else if (off < 0 || len < 0 || len > buffer.length - off) {
      throw new IndexOutOfBoundsException();
    } else if (len == 0) {
      return;
    }

    for (int i = 0; i < len; i++) {
      final int b = readUnsignedByte();

      if (b == -1) {
        break;
      }

      buffer[off + i] = (byte) b;
    }
  }

  /**
   * Read a boolean from the underlying input stream.
   *
   * @return <code>true</code> if the 8 bit byte value is not 0
   * @throws IOException
   *           from underlying input stream
   */
  public boolean readBoolean() throws IOException {
    return readByte() != 0;
  }

  /**
   * Read a signed 8 bit byte from the underlying input stream.
   *
   * @return the byte value (-0x80 .. 0x7F)
   * @throws IOException
   *           from underlying input stream
   */
  public byte readByte() throws IOException {
    return (byte) readUnsignedByte();
  }

  /**
   * Read a signed 32 bit int from the underlying input stream.
   *
   * @return the int value (-0x80000000 .. 0x7FFFFFFF)
   * @throws IOException
   *           from underlying input stream
   */
  public int readInt() throws IOException {
    return (int) readUnsignedInt();
  }

  /**
   * Read a signed 64 bit long from the underlying input stream.
   *
   * @return the long value (-0x8000000000000000 .. 0x7FFFFFFFFFFFFFFF)
   * @throws IOException
   *           from underlying input stream
   */
  public long readLong() throws IOException {
    return readUnsignedInt() + (readUnsignedInt() << Integer.SIZE);
  }

  /**
   * Read a signed 16 bit short from the underlying input stream.
   *
   * @return the short value (-0x8000 .. 0x7FFF)
   * @throws IOException
   *           from underlying input stream
   */
  public short readShort() throws IOException {
    return (short) readUnsignedShort();
  }

  /**
   * Read the specified number of bytes and return them as a string.
   *
   * @param length
   *          the number of bytes in the string
   * @return the resulting string
   * @throws IOException
   *           from underlying input stream
   */
  public String readString(final int length) throws IOException {
    final byte[] bytes = new byte[length];
    read(bytes);
    return new String(bytes, ISO_8859_1);
  }

  /**
   * Read an unsigned 8 bit byte from the underlying input stream.
   *
   * @return the unsigned byte value (0x00 ... 0xFF). Only the lower 8 bit of the result are used.
   * @throws IOException
   *           from underlying input stream
   */
  public int readUnsignedByte() throws IOException {
    final int result = inputStream.read();
    crc.update((byte) result);
    offset++;

    return result;
  }

  /**
   * Read an unsigned 32 bit int from the underlying input stream.
   *
   * @return the unsigned int value (0x00000000 ... 0xFFFFFFFF). Only the lower 32 bit of the result are used.
   * @throws IOException
   *           from underlying input stream
   */
  public long readUnsignedInt() throws IOException {
    return readUnsignedShort() + ((long) readUnsignedShort() << Short.SIZE);
  }

  /**
   * Read an unsigned 16 bit short from the underlying input stream.
   *
   * @return the unsigned short value (0x0000 .. 0xFFFF). Only the lower 16 bit of the result are used.
   * @throws IOException
   *           from underlying input stream
   */
  public int readUnsignedShort() throws IOException {
    return readUnsignedByte() + (readUnsignedByte() << Byte.SIZE);
  }

  /**
   * Reset the CRC-16 checksum. Call this method at the begin of a data block. The checksum will be calculated while data is read.
   */
  public void resetCRC() {
    crc.reset();
  }

  /**
   * Skip over the specified number of bytes from the underlying input steam.
   *
   * @param n
   *          the number of bytes to skip
   * @throws IOException
   *           from the underlying input stream
   */
  public void skip(final int n) throws IOException {
    int count = n;
    while (count-- > 0) {
      readByte();
    }
  }
}