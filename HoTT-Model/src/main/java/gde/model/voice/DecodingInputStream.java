package gde.model.voice;

import java.io.IOException;
import java.io.InputStream;

final class DecodingInputStream extends InputStream {
    private final ADPCMCodec codec = new ADPCMCodec();
    private final InputStream in;
    private final int[] buffer = new int[4];
    private int index = 4;

    public DecodingInputStream(final InputStream in) {
        this.in = in;
    }

    @Override
    public int available() throws IOException {
        // decoding enlarges the data size by factor of 4
        return in.available() * 4;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

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
        if (index > 3) {
            // buffer underflow - fetch next value from stream
            final int b = in.read();

            if (b == -1) return -1; // end of stream

            index = 0; // reset buffer

            final byte lowNibble = (byte) (b & 0x0F);
            final byte highNibble = (byte) ((b & 0xF0) >> 4);
            short pcm;

            // decode low nibble
            pcm = (short) (codec.decode(lowNibble) * 16); // convert 12-bit to 16-bit
            buffer[0] = pcm & 0x00ff; // low byte
            buffer[1] = (pcm & 0xff00) >> 8; // high byte

            // decode high nibble
            pcm = (short) (codec.decode(highNibble) * 16); // convert 12-bit to 16-bit
            buffer[2] = pcm & 0x00ff; // low byte
            buffer[3] = (pcm & 0xff00) >> 8; // high byte
        }

        return buffer[index++];
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