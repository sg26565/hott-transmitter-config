package gde.model.voice;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

public abstract class PCMFilterInputStream extends InputStream {
    protected final Queue<Integer> buffer = new LinkedList<>();
    protected final InputStream in;

    public PCMFilterInputStream(final InputStream in) {
        this.in = in;
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    protected abstract short filter(short s);

    @Override
    public synchronized void mark(final int readlimit) {
        in.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return in.markSupported();
    }

    @Override
    public int read() throws IOException {
        if (buffer.isEmpty()) {
            final short s = readShort();
            final short s1 = filter(s);
            buffer.add(s1 & 0x00ff);
            buffer.add(s1 >> 8 & 0x00ff);
        }

        return buffer.remove();
    }

    protected short readShort() throws IOException {
        final int i1 = in.read();

        if (i1 == -1) return -1;

        final int i2 = in.read();

        if (i2 == -1) return (short) i1;

        return (short) (i1 + (i2 << 8));
    }

    @Override
    public synchronized void reset() throws IOException {
        in.reset();
    }

    @Override
    public long skip(final long n) throws IOException {
        return in.skip(n);
    }
}
