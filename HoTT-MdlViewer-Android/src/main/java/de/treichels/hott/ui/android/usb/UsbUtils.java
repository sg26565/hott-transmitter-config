package de.treichels.hott.ui.android.usb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

public class UsbUtils {
  /**
   * Get a list of all USB Devices currently attached.
   *
   * @param context
   * @return
   */
  public static List<UsbDevice> getDevices(final Context context) {
    if (DEVICES == null) {
      DEVICES = new ArrayList<UsbDevice>();

      final UsbSerialProber prober = UsbSerialProber.getDefaultProber();

      // Check each device and filter on UsbSerial
      for (final UsbDevice device : getUsbManager(context).getDeviceList().values()) {
        final UsbSerialDriver driver = prober.probeDevice(device);
        if (driver != null) {
          DEVICES.add(device);
        }
      }
    }

    return DEVICES;
  }

  /**
   * Get the system wide {@linkp UsbManager}.
   *
   * @param context
   * @return
   */
  public static UsbManager getUsbManager(final Context context) {
    return (UsbManager) context.getSystemService(Context.USB_SERVICE);
  }

  /**
   * Check if the device is USB host capable only once and remember the result.
   *
   * @return
   */
  public static boolean isUsbHost(final Context context) {
    return !getDevices(context).isEmpty();
  }

  /**
   * List of USB host devices.
   */
  private static List<UsbDevice> DEVICES = null;
}
