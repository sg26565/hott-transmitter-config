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
package de.treichels.hott.android.background.serial.bluetooth;

import gde.model.HoTTException;
import gde.model.serial.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

/**
 * An adapter that turns a UsbSerialPort into a SerialPort for the HoTTDecoder.
 *
 * @author oli@treichels.de
 */
public class BluetoothSerialPortImplementation implements SerialPort {
  private final BluetoothSocket socket;

  /**
   * @param connection
   * @param device
   * @param interface1
   */
  public BluetoothSerialPortImplementation(final BluetoothSocket socket) {
    this.socket = socket;
  }

  @Override
  public void close() throws IOException {
    if (socket.isConnected()) {
      socket.close();
    }
  }

  @Override
  public InputStream getInputStream() throws HoTTException {
    try {
      return socket.getInputStream();
    } catch (final IOException e) {
      throw new HoTTException(e);
    }
  }

  @Override
  public OutputStream getOutputStream() throws HoTTException {
    try {
      return socket.getOutputStream();
    } catch (final IOException e) {
      throw new HoTTException(e);
    }
  }

  @Override
  public boolean isOpen() {
    return socket.isConnected();
  }

  @Override
  public void open() throws HoTTException {
    if (!socket.isConnected()) {
      try {
        socket.connect();
      } catch (final IOException e) {
        throw new HoTTException(e);
      }
    }
  }
}