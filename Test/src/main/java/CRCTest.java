import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import de.treichels.hott.internal.io.HoTTReader;

public class CRCTest {
    public static void main(final String[] args) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            final String raw = reader.readLine().toLowerCase();
            final int len = raw.length();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(len / 3);

            for (int i = 0; i < len; i += 3) {
                final char c1 = raw.charAt(i);
                final char c2 = raw.charAt(i + 1);
                final byte b1 = (byte) (c1 > '9' ? c1 - 'a' + 10 : c1 - '0');
                final byte b2 = (byte) (c2 > '9' ? c2 - 'a' + 10 : c2 - '0');
                final byte b = (byte) (b1 * 16 + b2);
                // System.out.printf("%c%c -> 0x%x%x -> 0x%02x%n", c1, c2, b1, b2, b);
                baos.write(b);
            }

            final HoTTReader r = new HoTTReader(baos.toByteArray());
            r.resetCRC();
            r.skip(len);
            System.out.printf("0x%04x%n", r.getCRC());
        }
    }
}
