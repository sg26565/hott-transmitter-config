package de.treichels.hott.ui.android.dialogs;

import android.hardware.usb.UsbDevice;
import de.treichels.hott.ui.android.tx.usb.UsbDeviceHandler;

public class OpenFromUsbMemoryDialog extends OpenFromMemoryDialog<UsbDevice> {
  @Override
  protected void setHandler(final UsbDevice device) {
    handler = new UsbDeviceHandler(getActivity(), device);
  }
}
