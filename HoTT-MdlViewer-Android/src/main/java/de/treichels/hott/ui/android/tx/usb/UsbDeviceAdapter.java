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
package de.treichels.hott.ui.android.tx.usb;

import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import de.treichels.hott.ui.android.tx.DeviceAdapter;
import de.treichels.hott.ui.android.tx.DeviceInfo;

/**
 * An {@link DeviceAdapter} that holds a list of {@link UsbDevice} objects.
 *
 * @author oli@treichels.de
 */
public class UsbDeviceAdapter extends DeviceAdapter<UsbDevice> {

  public UsbDeviceAdapter(final Context context) {
    super(context);

    final UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    final Map<String, UsbDevice> deviceMap = manager.getDeviceList();

    for (final Entry<String, UsbDevice> e : deviceMap.entrySet()) {
      final DeviceInfo<UsbDevice> info = new DeviceInfo<UsbDevice>();
      info.setName(e.getKey());
      info.setDevice(e.getValue());
      info.setId(e.getValue().getDeviceId());

      devices.add(info);
    }
  }
}
