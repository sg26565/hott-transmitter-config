package gde.model.voice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ADPCMCodec {
    private static final int[] QUANTIZER = { -1, -1, -1, -1, 2, 4, 6, 8 };
    private static final int[] STEP_SIZES = { 16, 17, 19, 21, 23, 25, 28, 31, 34, 37, 41, 45, 50, 55, 60, 66, 73, 80, 88, 97, 107, 118, 130, 143, 157, 173, 190,
            209, 230, 253, 279, 307, 337, 371, 408, 449, 494, 544, 598, 658, 724, 796, 876, 963, 1060, 1166, 1282, 1411, 1552 };
    private static final int MIN_PCM = Short.MIN_VALUE;
    private static final int MAX_PCM = Short.MAX_VALUE;
    private static final int MIN_INDEX = 0;
    private static final int MAX_INDEX = STEP_SIZES.length - 1;

    /** index into step size table */
    private int index = 0;

    /** last PCM value */
    private int last = 0;

    /**
     * Decode an ADPCM encoded 4-bit sample to 16-bit signed PCM.
     *
     * @param adpcm
     *            ADPCM encoded sample (4 bit)
     * @return The decoded PCM value (16 bit signed)
     */
    private int decode(final int adpcm) {
        // current step size
        final int ss = STEP_SIZES[index];

        // calculate difference
        int diff = ss / 8;
        // magnitude bit 3
        if ((adpcm & 0b0100) != 0) diff += ss;
        // magnitude bit 2
        if ((adpcm & 0b0010) != 0) diff += ss / 2;
        // magnitude bit 1
        if ((adpcm & 0b0001) != 0) diff += ss / 4;
        // sign
        if ((adpcm & 0b1000) != 0) diff = -diff;

        // new PCM value
        last += diff;

        // clipping
        if (last > MAX_PCM) last = MAX_PCM;
        if (last < MIN_PCM) last = MIN_PCM;

        // new step size
        index += QUANTIZER[adpcm & 0b0111];

        // clipping
        if (index < MIN_INDEX) index = MIN_INDEX;
        if (index > MAX_INDEX) index = MAX_INDEX;

        return last;
    }

    /**
     * Decode 4-bit ADPCM encoded data to 16-bit signed PCM (compression ratio 1:4).
     *
     * @param adpcm
     *            ADPCM encoded data. Each byte contains two 4-bit samples.
     * @return The PCM decoded data.
     * @throws IOException
     */
    public byte[] decodeADPCM(final byte[] adpcm) throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(adpcm);
        final ByteArrayOutputStream out = new ByteArrayOutputStream(adpcm.length * 4);
        decodeADPCM(in, out);
        return out.toByteArray();
    }

    /**
     * Decode 4-bit ADPCM encoded data to 16-bit signed PCM (compression ratio 1:4).
     *
     * @param in
     *            A stream of ADPCM encoded data. Each byte contains two 4-bit samples.
     * @param out
     *            A stream of PCM data.
     * @throws IOException
     */
    public void decodeADPCM(final InputStream in, final OutputStream out) throws IOException {
        reset();

        while (in.available() > 0) {
            final int b = in.read();
            int pcm;

            // decode high nibble
            pcm = decode((b & 0xf0) >> 4);
            // write low byte
            out.write(pcm & 0x00ff);
            // write high byte
            out.write((pcm & 0xff00) >> 8);

            // decode low nibble
            pcm = decode(b & 0x0f);
            // write low byte
            out.write(pcm & 0x00ff);
            // write high byte
            out.write((pcm & 0xff00) >> 8);
        }
    }

    /**
     * Encode a 16-bit signed PCM value to 4-bit ADPCM.
     *
     * @param pcm
     *            The PCM value (16 bit signed)
     * @return The ADPCM encoded sample (4 bit)
     */
    private int encode(final int pcm) {
        final int ss = STEP_SIZES[index];
        int diff = pcm - last;
        int adpcm = 0;

        // sign
        if (diff < 0) {
            adpcm |= 0x1000;
            diff = -diff;
        }

        // magnitude bit 3
        if (diff > ss) {
            adpcm |= 0b0100;
            diff -= ss;
        }

        // magnitude bit 2
        if (diff > ss / 2) {
            adpcm |= 0b0010;
            diff -= ss / 2;
        }

        // magnitude bit 1
        if (diff > ss / 4) adpcm |= 0b0001;

        last = decode(adpcm);

        return adpcm;
    }

    /**
     * Encode 16-bit signed PCM data to 4-bit ADPCM encoded data (compression ratio 4:1).
     *
     * @param pcm
     *            The PCM data.
     *
     * @return The ADPCM encoded data. Each byte contains two 4-bit samples.
     * @throws IOException
     */
    public byte[] encodeADPCM(final byte[] pcm) throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(pcm);
        final ByteArrayOutputStream out = new ByteArrayOutputStream(pcm.length / 4);
        encodeADPCM(in, out);
        return out.toByteArray();
    }

    /**
     * Encode 16-bit signed PCM data to 4-bit ADPCM encoded data (compression ratio 4:1).
     *
     * @param in
     *            A stream of PCM data.
     * @param out
     *            A stream of ADPCM encoded data. Each byte contains two 4-bit samples.
     * @throws IOException
     */
    public void encodeADPCM(final InputStream in, final OutputStream out) throws IOException {
        reset();

        while (in.available() > 0) {
            int pcm, adpcm;

            // read 16 bit for high nibble
            pcm = in.read() + (in.read() << 8);
            adpcm = encode(pcm) << 4;

            if (in.available() > 0) {
                // read 16 bit for low nibble
                pcm = in.read() + (in.read() << 8);
                adpcm += encode(pcm);
            }

            out.write(adpcm);
        }
    }

    /**
     * Reset state of the encoder/decoder.
     */
    private void reset() {
        index = 0;
        last = 0;
    }
}
