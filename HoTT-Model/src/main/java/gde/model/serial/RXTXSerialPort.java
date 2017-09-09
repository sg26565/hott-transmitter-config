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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import gde.model.HoTTException;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public class RXTXSerialPort implements gde.model.serial.SerialPort {
    private static final int WRITE_BUFFER_SIZE = 2064;
    private static final int READ_BUFFER_SIZE = 2064;

    public static List<String> getAvailablePorts() {
        final List<String> result = new ArrayList<>();

        final Enumeration<CommPortIdentifier> enumeration = CommPortIdentifier.getPortIdentifiers();

        while (enumeration.hasMoreElements()) {
            final CommPortIdentifier identifier = enumeration.nextElement();
            if (identifier.getPortType() == CommPortIdentifier.PORT_SERIAL) result.add(identifier.getName());
        }

        return result;
    }

    private final String name;

    private SerialPort port = null;

    /**
     * @param name
     */
    public RXTXSerialPort(final String name) {
        this.name = name;
    }

    @Override
    public void close() {
        port.close();
        port = null;
    }

    @Override
    public InputStream getInputStream() throws HoTTException {
        try {
            return port.getInputStream();
        } catch (final IOException e) {
            throw new HoTTException(e);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public OutputStream getOutputStream() throws HoTTException {
        try {
            return port.getOutputStream();
        } catch (final IOException e) {
            throw new HoTTException(e);
        }
    }

    @Override
    public boolean isOpen() {
        return port != null;
    }

    @Override
    public void open() throws HoTTException {
        try {
            final CommPortIdentifier identifier = CommPortIdentifier.getPortIdentifier(name);
            port = (SerialPort) identifier.open(RXTXSerialPort.class.getName(), 1000);
            port.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            port.setDTR(false);
            port.setRTS(false);
            port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            port.setInputBufferSize(READ_BUFFER_SIZE);
            port.setOutputBufferSize(WRITE_BUFFER_SIZE);
        } catch (final NoSuchPortException e) {
            throw new HoTTException(e);
        } catch (final PortInUseException e) {
            throw new HoTTException(e);
        } catch (final UnsupportedCommOperationException e) {
            throw new HoTTException(e);
        }
    }
}
