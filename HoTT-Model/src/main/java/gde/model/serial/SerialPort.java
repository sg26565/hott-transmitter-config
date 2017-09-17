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

import java.io.InputStream;
import java.io.OutputStream;

import gde.model.HoTTException;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public interface SerialPort extends AutoCloseable {
    @Override
    public void close() throws HoTTException;

    public InputStream getInputStream() throws HoTTException;

    public OutputStream getOutputStream() throws HoTTException;

    public String getPortName();

    public boolean isOpen();

    public void open() throws HoTTException;

    public void reset() throws HoTTException;
}
