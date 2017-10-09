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
package de.treichels.hott.ui.android.dialogs.usb;

import android.hardware.usb.UsbDevice;
import de.treichels.hott.android.background.serial.DeviceHandler;
import de.treichels.hott.android.background.serial.usb.UsbDeviceHandler;
import de.treichels.hott.ui.android.R;
import de.treichels.hott.ui.android.dialogs.OpenFromMemoryDialog;

/**
 * Read list of model via USB.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class OpenFromMemoryDialogUsb extends OpenFromMemoryDialog<UsbDevice> {
  private UsbDeviceHandler handler = null;

  @Override
  public DeviceHandler<UsbDevice> getDeviceHandler() {
    if (handler == null) {
      handler = new UsbDeviceHandler(getActivity());
    }

    return handler;
  }

  @Override
  protected int getPortSelectorLabelId() {
    return R.string.usb_device;
  }

  @Override
  protected int getTitleId() {
    return R.string.action_load_from_tx_usb;
  }
}
