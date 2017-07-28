package gde.model.voice;

import java.io.IOException;
import java.io.InputStream;

final class EncodingInputStream extends InputStream {
    private final ADPCMCodec codec = new ADPCMCodec();
    private final InputStream source;

    EncodingInputStream(final InputStream source) {
        this.source = source;
    }

    @Override
    public int available() throws IOException {
        return (source.available() + 3) / 4;
    }

    @Override
    public void close() throws IOException {
        source.close();
    }

    @Override
    public int read() throws IOException {
        int pcm, adpcm;
        final byte[] frame = new byte[2];

        // read 16 bit for high nibble
        if (source.read(frame) == -1) return -1;
        pcm = frame[0] + (frame[1] << 8);
        adpcm = codec.encode(pcm >> 4) << 4; // convert 16-bit to 12-bit and shift to high nibble

        // read 16 bit for low nibble
        if (source.read(frame) > 0) {
            pcm = frame[0] + (frame[1] << 8);
            adpcm += codec.encode(pcm >> 4); // convert 16-bit to 12-bit
        }

        return adpcm;
    }
}