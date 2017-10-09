package gde.model.serial;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ByteBufferTest {
    @Test
    public void testBulkReadWrite() {
        final ByteBuffer b = new ByteBuffer(5);
        final byte[] data = new byte[3];

        assertEquals(5, b.write(new byte[] { 0, 1, 2, 3, 4 }));
        assertEquals(5, b.available());
        assertEquals(0, b.getIndex());
        assertEquals(5, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(0, b.remaining());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, b.getBuffer());

        assertEquals(0, b.write(new byte[] { 5, 6, 7 })); // buffer overflow
        assertEquals(5, b.available());
        assertEquals(0, b.getIndex());
        assertEquals(5, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(0, b.remaining());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, b.getBuffer());

        assertEquals(3, b.read(data));
        assertArrayEquals(new byte[] { 0, 1, 2 }, data);
        assertEquals(2, b.available());
        assertEquals(3, b.getIndex());
        assertEquals(5, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(3, b.remaining());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, b.getBuffer());

        assertEquals(2, b.read(data)); // buffer underflow
        assertArrayEquals(new byte[] { 3, 4, 2 }, data);
        assertEquals(0, b.available());
        assertEquals(5, b.getIndex());
        assertEquals(5, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(5, b.remaining());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, b.getBuffer());

        assertEquals(4, b.write(new byte[] { 5, 6, 7, 8 }));
        assertEquals(4, b.available());
        assertEquals(5, b.getIndex());
        assertEquals(9, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(1, b.remaining());
        assertArrayEquals(new byte[] { 5, 6, 7, 8, 4 }, b.getBuffer());

        assertEquals(3, b.read(data));
        assertArrayEquals(new byte[] { 5, 6, 7 }, data);
        assertEquals(1, b.available());
        assertEquals(8, b.getIndex());
        assertEquals(9, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(4, b.remaining());
        assertArrayEquals(new byte[] { 5, 6, 7, 8, 4 }, b.getBuffer());

        assertEquals(4, b.write(new byte[] { 9, 10, 11, 12, 13, 14, 15 }));
        assertEquals(5, b.available());
        assertEquals(8, b.getIndex());
        assertEquals(13, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(0, b.remaining());
        assertArrayEquals(new byte[] { 10, 11, 12, 8, 9 }, b.getBuffer());

        assertEquals(3, b.read(data));
        assertArrayEquals(new byte[] { 8, 9, 10 }, data);
        assertEquals(2, b.available());
        assertEquals(11, b.getIndex());
        assertEquals(13, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(3, b.remaining());
        assertArrayEquals(new byte[] { 10, 11, 12, 8, 9 }, b.getBuffer());

        assertEquals(2, b.read(data)); // buffer underflow
        assertArrayEquals(new byte[] { 11, 12, 10 }, data);
        assertEquals(0, b.available());
        assertEquals(13, b.getIndex());
        assertEquals(13, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(5, b.remaining());
        assertArrayEquals(new byte[] { 10, 11, 12, 8, 9 }, b.getBuffer());
    }

    @Test
    public void testByteBuffer() {
        final ByteBuffer b = new ByteBuffer(5);

        assertEquals(0, b.available());
        assertEquals(0, b.getIndex());
        assertEquals(0, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(5, b.remaining());
        assertEquals(-1, b.read());
        assertArrayEquals(new byte[] { 0, 0, 0, 0, 0 }, b.getBuffer());
    }

    @Test
    public void testReadWriteByte() {
        final ByteBuffer b = new ByteBuffer(5);

        // fill buffer (0 .. 4)
        for (byte i = 0; i < 5; i++) {
            assertEquals(i, b.available());
            assertEquals(0, b.getIndex());
            assertEquals(i, b.getLimit());
            assertEquals(5, b.getSize());
            assertEquals(5 - i, b.remaining());
            assertTrue(b.write(i));
        }

        // check if full
        assertFalse(b.write((byte) 5)); // buffer overflow
        assertEquals(5, b.available());
        assertEquals(0, b.getIndex());
        assertEquals(5, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(0, b.remaining());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, b.getBuffer());

        // read one byte (0)
        assertEquals(0, b.read());
        assertEquals(4, b.available());
        assertEquals(1, b.getIndex());
        assertEquals(5, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(1, b.remaining());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, b.getBuffer());

        // read one byte (1)
        assertEquals(1, b.read());
        assertEquals(3, b.available());
        assertEquals(2, b.getIndex());
        assertEquals(5, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(2, b.remaining());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, b.getBuffer());

        // read one byte (2)
        assertEquals(2, b.read());
        assertEquals(2, b.available());
        assertEquals(3, b.getIndex());
        assertEquals(5, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(3, b.remaining());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, b.getBuffer());

        // write one byte (5)
        assertTrue(b.write((byte) 5));
        assertEquals(3, b.available());
        assertEquals(3, b.getIndex());
        assertEquals(6, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(2, b.remaining());
        assertArrayEquals(new byte[] { 5, 1, 2, 3, 4 }, b.getBuffer());

        // write one byte (6)
        assertTrue(b.write((byte) 6));
        assertEquals(4, b.available());
        assertEquals(3, b.getIndex());
        assertEquals(7, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(1, b.remaining());
        assertArrayEquals(new byte[] { 5, 6, 2, 3, 4 }, b.getBuffer());

        // read one byte (3)
        assertEquals(3, b.read());
        assertEquals(3, b.available());
        assertEquals(4, b.getIndex());
        assertEquals(7, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(2, b.remaining());
        assertArrayEquals(new byte[] { 5, 6, 2, 3, 4 }, b.getBuffer());

        // read one byte (4)
        assertEquals(4, b.read());
        assertEquals(2, b.available());
        assertEquals(5, b.getIndex());
        assertEquals(7, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(3, b.remaining());
        assertArrayEquals(new byte[] { 5, 6, 2, 3, 4 }, b.getBuffer());

        // read one byte (5)
        assertEquals(5, b.read());
        assertEquals(1, b.available());
        assertEquals(6, b.getIndex());
        assertEquals(7, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(4, b.remaining());
        assertArrayEquals(new byte[] { 5, 6, 2, 3, 4 }, b.getBuffer());

        // read one byte (6)
        assertEquals(6, b.read());
        assertEquals(0, b.available());
        assertEquals(7, b.getIndex());
        assertEquals(7, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(5, b.remaining());
        assertArrayEquals(new byte[] { 5, 6, 2, 3, 4 }, b.getBuffer());

        // read one byte - buffer underflow
        assertEquals(-1, b.read());
        assertEquals(0, b.available());
        assertEquals(7, b.getIndex());
        assertEquals(7, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(5, b.remaining());
        assertArrayEquals(new byte[] { 5, 6, 2, 3, 4 }, b.getBuffer());

        // write one byte (7)
        assertTrue(b.write((byte) 7));
        assertEquals(1, b.available());
        assertEquals(7, b.getIndex());
        assertEquals(8, b.getLimit());
        assertEquals(5, b.getSize());
        assertEquals(4, b.remaining());
        assertArrayEquals(new byte[] { 5, 6, 7, 3, 4 }, b.getBuffer());
    }
}
