/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package gde.model.serial;

import static jssc.SerialPort.BAUDRATE_115200;
import static jssc.SerialPort.DATABITS_8;
import static jssc.SerialPort.FLOWCONTROL_NONE;
import static jssc.SerialPort.MASK_RXCHAR;
import static jssc.SerialPort.MASK_TXEMPTY;
import static jssc.SerialPort.PARITY_NONE;
import static jssc.SerialPort.PURGE_RXABORT;
import static jssc.SerialPort.PURGE_RXCLEAR;
import static jssc.SerialPort.PURGE_TXABORT;
import static jssc.SerialPort.PURGE_TXCLEAR;
import static jssc.SerialPort.STOPBITS_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import gde.model.HoTTException;
import gde.util.Util;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public class JSSCSerialPort implements SerialPort, SerialPortEventListener {
    private final class SerialPortInputStream extends InputStream {
        @Override
        public int available() throws IOException {
            return readBuffer.available();
        }

        @Override
        public int read() throws IOException {
            // block until data is available
            while (readBuffer.available() == 0) {
                try {
                    readFromPort();
                } catch (final SerialPortException e) {
                    throw new IOException(e);
                }

                if (readBuffer.available() == 0) readBuffer.waitRead(1);
            }

            return readBuffer.read();
        }
    }

    private final class SerialPortOutPutStream extends OutputStream {
        @Override
        public void flush() throws IOException {
            try {
                writeToPort();
            } catch (final SerialPortException e) {
                throw new IOException(e);
            }
        }

        @Override
        public void write(final int b) throws IOException {
            if (writeBuffer.remaining() == 0) {
                flush();
                writeBuffer.waitWrite(1);
            }

            writeBuffer.write(b & 0xff);
        }
    }

    private static boolean DEBUG = Util.DEBUG;
    private static final int READ_BUFFER_SIZE = 4096;
    private static final int WRITE_BUFFER_SIZE = 4096;

    public static List<String> getAvailablePorts() {
        return Arrays.asList(SerialPortList.getPortNames());
    }

    private final ByteBuffer readBuffer = new ByteBuffer(READ_BUFFER_SIZE);;
    private final ByteBuffer writeBuffer = new ByteBuffer(WRITE_BUFFER_SIZE);

    /** The name of the serial port. */
    private final String name;

    /** The internal low-level serial port implementation. */
    private jssc.SerialPort port = null;

    public JSSCSerialPort(final String name) {
        this.name = name;
    }

    @Override
    public void close() throws HoTTException {
        try {
            if (isOpen()) port.closePort();
        } catch (final SerialPortException e) {
            throw new HoTTException(e);
        } finally {
            port = null;
        }
    }

    @Override
    public InputStream getInputStream() throws HoTTException {
        return new SerialPortInputStream();
    }

    public String getName() {
        return name;
    }

    @Override
    public OutputStream getOutputStream() throws HoTTException {
        return new SerialPortOutPutStream();
    }

    @Override
    public String getPortName() {
        return name;
    }

    @Override
    public boolean isOpen() {
        return port != null && port.isOpened();
    }

    @Override
    public void open() throws HoTTException {
        if (isOpen()) throw new HoTTException("HoTTSerialPort.AlreadyOpen");

        try {
            port = new jssc.SerialPort(name);
            port.openPort();
            port.setParams(BAUDRATE_115200, DATABITS_8, STOPBITS_1, PARITY_NONE, false, false);
            port.setFlowControlMode(FLOWCONTROL_NONE);
            port.addEventListener(this, MASK_RXCHAR + MASK_TXEMPTY);
        } catch (final SerialPortException e) {
            throw new HoTTException(e);
        }
    }

    private void readFromPort() throws SerialPortException, IOException {
        if (DEBUG) System.out.printf("readFromPort: %d bytes available%n", port.getInputBufferBytesCount());

        final byte[] bytes = port.readBytes();
        if (bytes != null) readBuffer.write(bytes);
    }

    @Override
    public void reset() throws HoTTException {
        try {
            readBuffer.reset();
            writeBuffer.reset();
            port.purgePort(PURGE_RXABORT + PURGE_RXCLEAR + PURGE_TXABORT + PURGE_TXCLEAR);
        } catch (final SerialPortException e) {
            throw new HoTTException(e);
        }
    }

    @Override
    public void serialEvent(final SerialPortEvent event) {
        if (DEBUG) System.out.printf("serialEvent: port=%s, type=%s(%d), value=%d%n", event.getPortName(), event.getEventType() == 1 ? "RXCHAR" : "TXEMPTY",
                event.getEventType(), event.getEventValue());

        try {
            if (event.isRXCHAR()) readFromPort();

            if (event.isTXEMPTY()) writeToPort();
        } catch (final SerialPortException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToPort() throws SerialPortException {
        final int available = writeBuffer.available();

        if (available > 0) {
            if (DEBUG) System.out.printf("writeToPort: %d bytes to write%n", available);
            // write max 512 bytes to serial port
            final byte[] bytes = new byte[Math.min(available, 512)];
            writeBuffer.read(bytes);
            port.writeBytes(bytes);
        }
    }
}
