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
package de.treichels.hott.model.serial;

import de.treichels.hott.model.HoTTException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public interface SerialPort extends AutoCloseable {
    @Override
    void close() throws HoTTException;

    InputStream getInputStream() throws HoTTException;

    OutputStream getOutputStream() throws HoTTException;

    String getPortName();

    boolean isOpen();

    void open() throws HoTTException;

    void reset() throws HoTTException;
}
