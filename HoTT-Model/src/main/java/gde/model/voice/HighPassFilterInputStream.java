package gde.model.voice;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HighPassFilterInputStream extends FilterInputStream {
    private static final int[] WEIGHTS = { 1, 2, 4, 2, 1 };
    private static final int SUM = 10;
    private final Buffer buffer = new Buffer(5);

    protected HighPassFilterInputStream(final InputStream in) throws IOException {
        super(in);

        // fill buffer
        buffer.add(in.read());
        buffer.add(in.read());
        buffer.add(in.read());
        buffer.add(in.read());
    }

    @Override
    public int read() throws IOException {
        final int in = super.read();
        int out = 0;

        if (in == -1) return in;

        buffer.add(in);

        for (int i = 0; i < 5; i++)
            out += buffer.get(i) * WEIGHTS[i];

        return out / SUM;
    }
}
