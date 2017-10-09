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

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import de.treichels.hott.android.background.serial.DeviceAdapter;

/**
 * An {@link DeviceAdapter} that holds a list of {@link UsbDevice} objects.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class UsbDeviceAdapter extends DeviceAdapter<UsbDevice> {

  public UsbDeviceAdapter(final Context context) {
    super(context);

    final UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

    for (final UsbDevice device : manager.getDeviceList().values()) {
      devices.add(UsbDeviceHandler.getDeviceInfo(device));
    }
  }
}
