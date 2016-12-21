import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *  HoTT Transmitter Config
 *  Copyright (C) 2013  Oliver Treichel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @author oli@treichels.de
 */
public class ByteBufferTest {
	public static void main(final String[] args) {
		final byte[] data = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };
		final ByteBuffer bb = ByteBuffer.wrap(data);
		bb.order(ByteOrder.LITTLE_ENDIAN);

		System.out.printf("Short: %#04x (%1$d)\n", bb.getShort(0));
		System.out.printf("Short: %#08x (%1$d)\n", bb.getInt(0));
		System.out.printf("Short: %#016x (%1$d)\n", bb.getLong(0));
	}
}
