package gde.model.voice;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;

public class WeightedAverageFilterInputStream extends PCMFilterInputStream {
    private static final int[] WEIGHTS = { 1, 2, 8, 2, 1 };
    private static final int SUM = IntStream.of(WEIGHTS).sum();

    private final Buffer buffer = new Buffer(5);

    public WeightedAverageFilterInputStream(final InputStream in) throws IOException {
        super(in);

        // fill buffer
        buffer.add(readShort());
        buffer.add(readShort());
        buffer.add(readShort());
        buffer.add(readShort());
        buffer.add(readShort());
        buffer.add(readShort());
    }

    @Override
    protected short filter(final short s) {
        int out = 0;

        buffer.add(s);
        for (int i = 0; i < 5; i++)
            out += buffer.get(i) * WEIGHTS[i];

        return (short) (out / SUM);
    }
}
