package gde.model.voice;

import java.io.InputStream;

public class LowPassFilterInputStream extends PCMFilterInputStream {
    private final double smoothing;
    private short last = 0;

    public LowPassFilterInputStream(final InputStream in, final double d) {
        super(in);
        smoothing = d;
    }

    @Override
    protected short filter(final short s) {
        last += (s - last) * smoothing;

        return last;
    }
}
