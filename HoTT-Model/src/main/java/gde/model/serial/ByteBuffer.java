package gde.model.serial;

import gde.util.Util;

public class ByteBuffer {
    private static boolean DEBUG = Util.DEBUG;

    private final int size;
    private final byte[] buffer;
    private long index = 0;
    private long limit = 0;

    public ByteBuffer(final int size) {
        this.size = size;
        buffer = new byte[size];
    }

    /** The number of bytes available for read. */
    public int available() {
        if (DEBUG) System.out.printf("available=%d%n", limit - index);
        return (int) (limit - index);
    }

    /** The buffer holds data from {@link #index} inclusive to {@link #limit} exclusive. */
    public byte[] getBuffer() {
        return buffer;
    }

    /** Read position in the buffer. Any read will increment the index. The next byte to be read from is {@link #buffer}[{@link #index} % {@link #size}] */
    public long getIndex() {
        return index;
    }

    /** Write position in the buffer. Any write will increment the limit. The next byte to be written is {@link #buffer}[{@link #limit} % {@link #size}] */
    public long getLimit() {
        return limit;
    }

    /** The number of bytes that the buffer can hold. */
    public int getSize() {
        return size;
    }

    /** Read a single byte from the buffer. Return -1 if buffer was empty. */
    public synchronized int read() {
        // buffer underflow
        if (available() == 0) return -1;

        // index in buffer
        final int i = (int) (index % size);

        // increment index
        index++;
        if (DEBUG) System.out.printf("read: index=%d(%d), limit=%d, byte=%d%n", index, i, limit, buffer[i]);

        return buffer[i] & 0xff;
    }

    /** Bulk read method. Read up to data.length bytes from the buffer. Return the actual number of bytes read. */
    public synchronized int read(final byte[] data) {
        // buffer underflow
        if (available() == 0) return 0;

        // index in buffer
        final int i = (int) (index % size);

        // increment index
        final int len = Math.min(data.length, available());
        index += len;

        // space from index to end of buffer
        final int x = size - i;

        if (len <= x)
            // read data in one piece
            System.arraycopy(buffer, i, data, 0, len);
        else {
            // split and wrap around
            System.arraycopy(buffer, i, data, 0, x);
            System.arraycopy(buffer, 0, data, x, len - x);
        }

        if (DEBUG) System.out.printf("read: index=%d(%d, %d), limit=%d%n%s", index, i, x, limit, Util.dumpData(data));
        return len;
    }

    /** The number of bytes remaining for write. */
    public int remaining() {
        if (DEBUG) System.out.printf("remaining=%d%n", size - limit + index);
        return size - available();
    }

    /** Bulk write method. Write up to data.length bytes to the buffer. Return the actual number of bytes written. */
    public synchronized int write(final byte[] data) {
        // buffer overflow
        if (remaining() == 0) return 0;

        // limit in buffer
        final int l = (int) (limit % size);

        // increment limit
        final int len = Math.min(data.length, remaining());
        limit += len;

        // space from limit to end of buffer
        final int x = size - l;

        if (len <= x)
            // write data in one piece
            System.arraycopy(data, 0, buffer, l, len);
        else {
            // split and wrap around
            System.arraycopy(data, 0, buffer, l, x);
            System.arraycopy(data, x, buffer, 0, len - x);
        }

        if (DEBUG) System.out.printf("write: index=%d, limit=%d(%d, %d)%n%s", index, limit, l, x, Util.dumpData(data));
        return len;
    }

    /** Write a single byte to the buffer. Return false if buffer was full. */
    public synchronized boolean write(final int b) {
        // buffer overflow
        if (remaining() == 0) return false;

        // limit in buffer
        final int l = (int) (limit % size);

        // increment limit
        limit++;

        buffer[l] = (byte) b;
        if (DEBUG) System.out.printf("write: index=%d, limit=%d(%d), byte=%d%n", index, limit, l, b);
        return true;
    }
}
