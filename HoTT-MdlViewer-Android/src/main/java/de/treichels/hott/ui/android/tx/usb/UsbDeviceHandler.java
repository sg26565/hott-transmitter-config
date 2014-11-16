package de.treichels.hott.ui.android.tx.usb;

import gde.model.serial.SerialPort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import de.treichels.hott.ui.android.tx.DeviceHandler;
import de.treichels.hott.ui.android.tx.TxTask;

public class UsbDeviceHandler extends DeviceHandler<UsbDevice> {
  /**
   * A broadcast receiver that waits for {@link TxTask.ACTION_USB_PERMISSION} action intent and notifies a waiting thread.
   *
   * @author oli
   */
  final class Unlocker extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
      final String action = intent.getAction();
      if (ACTION_USB_PERMISSION.equals(action)) {
        synchronized (UsbDeviceHandler.this) {
          // wakeup main thread
          UsbDeviceHandler.this.notify();
        }
      }
    }
  }

  public static boolean isUsbHost(final Context context) {
    return new UsbDeviceHandler(context, null).isUsbHost();
  }

  /** Constant for the USB permission action. */
  public static final String  ACTION_USB_PERMISSION = "de.treichels.hott.ui.android.USB_PERMISSION";

  private final UsbManager    manager;

  private UsbDeviceConnection connection            = null;

  private SerialPort          impl                  = null;

  public UsbDeviceHandler(final Context context, final UsbDevice device) {
    super(context, device);
    manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
  }

  /**
   * Check that this activity has permission to access the USB device and ask user for permission if necessary.
   *
   * @param device
   */
  protected boolean check4Permission() {
    if (!manager.hasPermission(device)) {
      synchronized (this) {
        // setup broadcast receiver
        context.registerReceiver(new Unlocker(), new IntentFilter(UsbDeviceHandler.ACTION_USB_PERMISSION));

        // intent to fire on completion
        final PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(UsbDeviceHandler.ACTION_USB_PERMISSION), 0);

        // ask for permission
        manager.requestPermission(device, permissionIntent);

        try {
          // wait for user decision
          wait();
        } catch (final InterruptedException e) {
          // ignore
        }
      }
    }

    return manager.hasPermission(device);
  }

  @Override
  public void closeDevice() throws IOException {
    if (impl.isOpen()) {
      impl.close();
    }
    impl = null;

    if (connection != null) {
      connection.close();
      connection = null;
    }

  }

  @Override
  public String getDeviceId() {
    return device.getDeviceName();
  }

  @Override
  public SerialPort getPort() {
    return impl;
  }

  /**
   * Get a list of all USB Devices currently attached in host mode.
   *
   * @param context
   * @return
   */
  public synchronized List<UsbDevice> getUsbDevices() {
    final List<UsbDevice> list = new ArrayList<UsbDevice>();
    final UsbSerialProber prober = UsbSerialProber.getDefaultProber();

    // Check each device and filter on UsbSerial
    for (final UsbDevice device : manager.getDeviceList().values()) {
      final UsbSerialDriver driver = prober.probeDevice(device);
      if (driver != null) {
        list.add(device);
      }
    }

    return list;
  }

  /**
   * Check if the device is USB host capable only once and remember the result.
   *
   * @return
   */
  public boolean isUsbHost() {
    return !getUsbDevices().isEmpty();
  }

  @Override
  public void openDevice() {
    check4Permission();
    final UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);

    if (driver != null) {
      final List<UsbSerialPort> ports = driver.getPorts();

      if (ports != null && ports.size() > 0) {
        connection = manager.openDevice(device);
        impl = new UsbSerialPortImplementation(ports.get(0), connection);
      }
    }
  }
}