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
package de.treichels.hott.android.background.serial;

import de.treichels.hott.model.serial.SerialPort;

import java.io.IOException;

import android.content.Context;

/**
 * A generic handler for communication devices. This allows to use the same code base for comminucation with the transmitter regardless of communication channel
 * in use. There are separate implementations of this class for USB and Bluetooth.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public abstract class DeviceHandler<DeviceType> {
  protected final Context context;

  protected DeviceHandler(final Context context) {
    this.context = context;
  }

  public abstract void closeDevice() throws IOException;

  public Context getContext() {
    return context;
  }

  public DeviceType getDevice() {
    return getDeviceInfo().getDevice();
  }

  public abstract DeviceAdapter<DeviceType> getDeviceAdapter();

  public long getDeviceId() {
    return getDeviceInfo().getId();
  }

  public abstract DeviceInfo<DeviceType> getDeviceInfo();

  public String getDeviceName() {
    return getDeviceInfo().getName();
  }

  public abstract SerialPort getPort();

  public abstract String getPreferenceKey();

  public abstract void openDevice() throws IOException;

  public abstract void setDevice(DeviceType device);
}
