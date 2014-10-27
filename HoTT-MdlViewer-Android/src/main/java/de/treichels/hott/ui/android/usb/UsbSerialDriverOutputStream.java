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
package de.treichels.hott.ui.android.usb;

import java.io.IOException;
import java.io.OutputStream;

import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialPort;

/**
 * An {@link OutputStream} that writes to a supplied {@link UsbSerialPort}.
 *
 * @author oli@treichels.de
 */
class UsbSerialDriverOutputStream extends OutputStream {
    private final UsbSerialPort port;
    private final byte[]        buffer = new byte[AndroidUsbSerialPortImplementation.BUFFER_SIZE];
    private int                 len    = 0;

    /**
     * @param port
     */
    public UsbSerialDriverOutputStream(final UsbSerialPort port) {
        this.port = port;
    }

    @Override
    public void flush() throws IOException {
        if (len > 0) {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < len; i++) {
                builder.append(String.format("%02x ", buffer[i]));
            }

            byte[] b;
            if (len == AndroidUsbSerialPortImplementation.BUFFER_SIZE) {
                b = buffer;
            } else {
                b = new byte[len];
                System.arraycopy(buffer, 0, b, 0, len);
            }

            final int rc = port.write(b, AndroidUsbSerialPortImplementation.IO_TIMEOUT);
            builder.append(": ").append(rc);

            Log.i("bulkUsbSerialDriver.write()", builder.toString());

            len = 0;
        }
    }

    @Override
    public void write(final int oneByte) throws IOException {
        buffer[len++] = (byte) oneByte;

        if (len == AndroidUsbSerialPortImplementation.BUFFER_SIZE) {
            flush();
        }
    }
}