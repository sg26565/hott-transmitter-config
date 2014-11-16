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
package de.treichels.hott.ui.android.tx.usb;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialPort;

/**
 * An {@link InputStream} that reads from a supplied {@link UsbSerialPort}.
 *
 * @author oli@treichels.de
 */
class UsbSerialDriverInputStream extends InputStream {
    private final byte[]        buffer = new byte[UsbSerialPortImplementation.BUFFER_SIZE];
    private int                 index  = 0;
    private int                 len    = 0;
    private final UsbSerialPort port;

    /**
     * @param port
     */
    public UsbSerialDriverInputStream(final UsbSerialPort port) {
        this.port = port;
    }

    @Override
    public int available() throws IOException {
        int available = len - index;

        if (available == 0) {
            available = fetch();
        }

        return available;
    }

    private int fetch() throws IOException {
        index = 0;
        len = port.read(buffer, UsbSerialPortImplementation.IO_TIMEOUT);

        if (len < 0) {
            Log.i("UsbSerialDriver.read()", "no data");
            len = 0;
        } else {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < len; i++) {
                builder.append(String.format("%02x ", buffer[i]));
            }
            Log.i("UsbSerialDriver.read()", builder.toString());
        }

        return len;
    }

    @Override
    public int read() throws IOException {
        if (available() == 0) {
            fetch();
        }

        return index < len ? buffer[index++] & 0xff : -1;
    }
}