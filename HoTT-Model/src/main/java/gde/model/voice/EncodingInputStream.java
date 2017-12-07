package gde.model.voice;

import java.io.IOException;
import java.io.InputStream;

final class EncodingInputStream extends InputStream {
    private final ADPCMCodec codec = new ADPCMCodec();
    private final byte[] buffer = new byte[2];
    private final InputStream in;
    private final double volume;

    public EncodingInputStream(final InputStream in, double volume) {
        this.in = in;
        this.volume = volume;
    }

    @Override
    public int available() throws IOException {
        // encoding reduces the data size by factor of 4
        return (in.available() + 3) / 4;
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
        // read 16-bit for low nibble (unsigned)
        int pcm = readShort();
        int adpcm = -1;

        if (pcm != -1) {
            // convert to signed 16-bit and reduce to 12-bit
            short signed = (short) (((short) pcm / 16) * volume);

            // convert to low nibble
            adpcm = codec.encode(signed);

            // read 16 bit for high nibble (unsigned)
            pcm = readShort();
            if (pcm != -1) {
                // convert to signed 16-bit and reduce to 12-bit
                signed = (short) (((short) pcm / 16) * volume);

                // convert to high nibble
                adpcm |= codec.encode(signed) << 4;
            }
        }

        return adpcm;
    }

    /**
     * Read unsigned 16-bit value from stream.
     *
     * @return
     * @throws IOException
     */
    private int readShort() throws IOException {
        final int len = in.read(buffer);
        return len == -1 ? -1 : buffer[0] & 0xFF | (buffer[1] & 0xFF) << 8;
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