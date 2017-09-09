package gde.model.serial;

import java.io.IOException;

import gde.util.Util;

public class ByteBuffer {
    private final boolean DEBUG = Util.DEBUG;
    private final int TIMEOUT = 10000;

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
        return (int) (limit - index);
    }

    private void block() throws InterruptedException {
        synchronized (buffer) {
            buffer.wait(1000);
        }
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

        unblock();

        return buffer[i] & 0xff;
    }

    /** Bulk read method. Read up to data.length bytes from the buffer. Return the actual number of bytes read. */
    public synchronized int read(final byte[] data) {
        final int available = available();

        // buffer underflow
        if (available == 0) return 0;

        // index in buffer
        final int i = (int) (index % size);

        // increment index
        final int len = Math.min(data.length, available);
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

        unblock();

        return len;
    }

    /** The number of bytes remaining for write. */
    public int remaining() {
        return size - available();
    }

    public void reset() {
        index = limit = 0;
    }

    private void unblock() {
        synchronized (buffer) {
            if (DEBUG) System.out.println("unclock");
            buffer.notifyAll();
        }
    }

    /**
     * Block current thread until at least amount bytes become available for reading.
     *
     * @throws InterruptedException
     */
    public void waitRead(final int amount) throws IOException {
        final long start = System.currentTimeMillis();
        while (available() < amount) {
            if (DEBUG) System.out.printf("Not enough data available for read, blocking: requested=%d, available=%d%n", amount, available());
            try {
                block();
            } catch (final InterruptedException e) {
                throw new IOException(e);
            }
            if (System.currentTimeMillis() - start > TIMEOUT) throw new IOException("read timeout");
        }
    }

    /**
     * Block current thread until at least amount bytes become available for writing.
     *
     * @throws InterruptedException
     */
    public void waitWrite(final int amount) throws IOException {
        final long start = System.currentTimeMillis();
        while (remaining() < amount) {
            if (DEBUG) System.out.printf("Not enough data available for write, blocking: requested=%d, available=%d%n", amount, available());
            try {
                block();
            } catch (final InterruptedException e) {
                throw new IOException(e);
            }
            if (System.currentTimeMillis() - start > TIMEOUT) throw new IOException("write timeout");
        }
    }

    /** Bulk write method. Write up to data.length bytes to the buffer. Return the actual number of bytes written. */
    public synchronized int write(final byte[] data) {
        final int remaining = remaining();

        // buffer overflow
        if (remaining == 0) {
            System.err.printf("buffer overflow: %d byte were lost!%n%s%n", data.length, Util.dumpData(data));
            return 0;
        }

        // limit in buffer
        final int l = (int) (limit % size);

        // increment limit
        final int len = Math.min(data.length, remaining);
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

        unblock();

        return len;
    }

    /** Write a single byte to the buffer. Return false if buffer was full. */
    public synchronized boolean write(final int b) {
        final int remaining = remaining();
        // buffer overflow
        if (remaining == 0) {
            System.err.printf("buffer overflow: %d wase lost!%n", b & 0xff);
            return false;
        }

        // limit in buffer
        final int l = (int) (limit % size);

        // increment limit
        limit++;

        buffer[l] = (byte) b;

        unblock();

        return true;
    }
}
