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
package de.treichels.hott.android.background.serial.usb;

import android.hardware.usb.UsbDeviceConnection;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import de.treichels.hott.model.HoTTException;
import de.treichels.hott.serial.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An adapter that turns a UsbSerialPort into a SerialPort for the HoTTDecoder.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class UsbSerialPortImplementation implements SerialPort {
    static final int                  BUFFER_SIZE  = 2064;
    static final int                  IO_TIMEOUT   = 1000;

    private final UsbDeviceConnection connection;
    private InputStream               inputStream  = null;
    private boolean                   open         = false;
    private OutputStream              outputStream = null;
    private final UsbSerialPort       port;

    /**
     * @param connection
     * @param device
     * @param interface1
     */
    public UsbSerialPortImplementation(final UsbSerialPort port, final UsbDeviceConnection connection) {
        this.port = port;
        this.connection = connection;
    }

    @Override
    public void close() throws IOException {
        if (open) {
            inputStream.close();
            inputStream = null;

            outputStream.flush();
            outputStream.close();
            outputStream = null;

            port.close();
            open = false;
        }
    }

    @Override
    public InputStream getInputStream() throws HoTTException {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() throws HoTTException {
        return outputStream;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void open() throws HoTTException {
        if (!open) {
            try {
                port.open(connection);
                port.setParameters(115200, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

                inputStream = new UsbSerialDriverInputStream(port);
                outputStream = new UsbSerialDriverOutputStream(port);

                open = true;
            } catch (final IOException e) {
                throw new HoTTException(e);
            }
        }
    }
}
