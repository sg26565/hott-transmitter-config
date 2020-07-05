package de.treichels.hott.android.background.serial.usb;

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
import de.treichels.hott.android.background.serial.DeviceAdapter;
import de.treichels.hott.android.background.serial.DeviceHandler;
import de.treichels.hott.android.background.serial.DeviceInfo;
import de.treichels.hott.android.background.serial.TxTask;
import de.treichels.hott.serial.SerialPort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link DeviceHandler} for {@link UsbDevice} objects.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class UsbDeviceHandler extends DeviceHandler<UsbDevice> {
  /**
   * A broadcast receiver that waits for {@link TxTask.ACTION_USB_PERMISSION} action intent and notifies a waiting thread.
   *
   * @author Oliver Treichel &lt;oli@treichels.de&gt;
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

  public static DeviceInfo<UsbDevice> getDeviceInfo(final UsbDevice device) {
    final DeviceInfo<UsbDevice> info = new DeviceInfo<UsbDevice>();

    info.setDevice(device);
    info.setName(device.getDeviceName());
    info.setId(device.getDeviceId());

    return info;
  }

  /**
   * Does the mobile device have any USB devices attached in usb host mode?
   *
   * @param context
   * @return
   */
  public static boolean isUsbHost(final Context context) {
    return new UsbDeviceHandler(context).isUsbHost();
  }

  /** Constant for the USB permission action. */
  public static final String    ACTION_USB_PERMISSION = "de.treichels.hott.ui.android.USB_PERMISSION";

  private UsbDeviceConnection   connection            = null;

  private DeviceInfo<UsbDevice> deviceInfo            = null;

  private SerialPort            impl                  = null;

  private final UsbManager      manager;

  public UsbDeviceHandler(final Context context) {
    super(context);
    manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
  }

  /**
   * Check that this activity has permission to access the USB device and ask user for permission if necessary.
   *
   * @param device
   */
  private boolean check4Permission() {
    if (!manager.hasPermission(getDevice())) {
      synchronized (this) {
        // setup broadcast receiver
        context.registerReceiver(new Unlocker(), new IntentFilter(UsbDeviceHandler.ACTION_USB_PERMISSION));

        // intent to fire on completion
        final PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(UsbDeviceHandler.ACTION_USB_PERMISSION), 0);

        // ask for permission
        manager.requestPermission(getDevice(), permissionIntent);

        try {
          // wait for user decision
          wait();
        } catch (final InterruptedException e) {
          // ignore
        }
      }
    }

    return manager.hasPermission(getDevice());
  }

  @Override
  public void closeDevice() throws IOException {
    if (impl != null && impl.isOpen()) {
      impl.close();
    }
    impl = null;

    if (connection != null) {
      connection.close();
    }
    connection = null;

  }

  @Override
  public DeviceAdapter<UsbDevice> getDeviceAdapter() {
    return new UsbDeviceAdapter(context);
  }

  @Override
  public DeviceInfo<UsbDevice> getDeviceInfo() {
    return deviceInfo;
  }

  @Override
  public SerialPort getPort() {
    return impl;
  }

  @Override
  public String getPreferenceKey() {
    return UsbDevice.class.getName();
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
    final UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(getDevice());

    if (driver != null) {
      final List<UsbSerialPort> ports = driver.getPorts();

      if (ports != null && ports.size() > 0) {
        connection = manager.openDevice(getDevice());
        impl = new UsbSerialPortImplementation(ports.get(0), connection);
      }
    }
  }

  @Override
  public void setDevice(final UsbDevice device) {
    if (device == null) {
      deviceInfo = null;
    } else {
      deviceInfo = getDeviceInfo(device);
    }
  }
}
