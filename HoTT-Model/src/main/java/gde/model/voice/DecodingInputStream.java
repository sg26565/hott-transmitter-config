package gde.model.voice;

import java.io.IOException;
import java.io.InputStream;

final class DecodingInputStream extends PCMFilterInputStream {
    private final ADPCMCodec codec = new ADPCMCodec();

    DecodingInputStream(final InputStream in) {
        super(in);
    }

    @Override
    public int available() throws IOException {
        return in.available() * 4;
    }

    @Override
    protected short filter(final short s) {
        return 0;
    }

    @Override
    public int read() throws IOException {
        if (buffer.isEmpty()) {
            final int b = in.read();
            int pcm;

            // end of stream
            if (b == -1) return -1;

            // decode high nibble
            pcm = codec.decode((b & 0xf0) >> 4) << 4; // convert 12-bit to 16-bit
            // write low byte
            buffer.add(pcm & 0x00ff);
            // write high byte
            buffer.add((pcm & 0xff00) >> 8);

            // decode low nibble
            pcm = codec.decode(b & 0x0f) << 4; // convert 12-bit to 16-bit
            // write low byte
            buffer.add(pcm & 0x00ff);
            // write high byte
            buffer.add((pcm & 0xff00) >> 8);
        }

        return buffer.remove();
    }
}