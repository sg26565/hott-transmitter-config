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
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public class XorTest {
	public static void main(final String[] args) {
		int i = 0;
		byte b = 0;
		byte b1;

		while (i < 1024) {
			b1 = (byte) (b ^ 0xff);

			System.out.printf("%04d: 0x%02x - 0x%02x\n", i, b, b1);

			i++;
			b++;
		}

		b = (byte) 0x84;
		b1 = (byte) (b ^ 0xff);

		System.out.printf("%04d: 0x%02x - 0x%02x\n", i, b, b1);

		b = (byte) 0xff;
		b1 = (byte) 0xff;
		final short s = (short) ((b & 0xFF) + ((b1 & 0xFF) << 8));

		System.out.printf("%04d: 0x%02x - 0x%02x\n", s, b, b1);

	}
}
