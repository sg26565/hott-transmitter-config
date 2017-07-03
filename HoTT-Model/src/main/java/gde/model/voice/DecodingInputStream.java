package gde.model.voice;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

final class DecodingInputStream extends InputStream {
    private final ADPCMCodec codec = new ADPCMCodec();
    private final InputStream source;
    private final Queue<Integer> buffer = new LinkedList<>();

    DecodingInputStream(final InputStream source) {
        this.source = source;
    }

    @Override
    public int available() throws IOException {
        return source.available() * 4;
    }

    @Override
    public void close() throws IOException {
        source.close();
    }

    @Override
    public int read() throws IOException {
        if (buffer.isEmpty()) {
            final int b = source.read();
            int pcm;

            // end of stream
            if (b == -1) return -1;

            // decode high nibble
            pcm = codec.decode((b & 0xf0) >> 4);
            // write low byte
            buffer.add(pcm & 0x00ff);
            // write high byte
            buffer.add((pcm & 0xff00) >> 8);

            // decode low nibble
            pcm = codec.decode(b & 0x0f);
            // write low byte
            buffer.add(pcm & 0x00ff);
            // write high byte
            buffer.add((pcm & 0xff00) >> 8);
        }

        return buffer.remove();
    }
}