package de.treichels.hott.ui.android.dialogs;

import android.hardware.usb.UsbDevice;
import de.treichels.hott.ui.android.tx.usb.UsbDeviceHandler;

public class OpenFromUsbSdDialog extends OpenFromSdDialog<UsbDevice> {
  @Override
  protected void setHandler(final UsbDevice device) {
    handler = new UsbDeviceHandler(getActivity(), device);
  }
}
