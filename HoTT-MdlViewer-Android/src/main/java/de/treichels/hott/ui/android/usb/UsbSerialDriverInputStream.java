package de.treichels.hott.ui.android.usb;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialPort;

/**
 * An {@link InputStream} that reads from a supplied {@link UsbSerialPort}.
 *
 * @author oli
 */
class UsbSerialDriverInputStream extends InputStream {
    private final UsbSerialPort port;
    private final byte[]        buffer = new byte[AndroidUsbSerialPortImplementation.BUFFER_SIZE];
    private int                 index  = 0;
    private int                 len    = 0;

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
        len = port.read(buffer, AndroidUsbSerialPortImplementation.IO_TIMEOUT);

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