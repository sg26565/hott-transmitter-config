package de.treichels.hott.ui.android.tx.bluetooth;

import gde.model.serial.SerialPort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.ParcelUuid;
import de.treichels.hott.ui.android.tx.DeviceAdapter;
import de.treichels.hott.ui.android.tx.DeviceHandler;
import de.treichels.hott.ui.android.tx.DeviceInfo;

/**
 * A {@link DeviceHandler} for {@link BluetoothDevice} objects.
 *
 * @author oli
 */
public class BluetoothDeviceHandler extends DeviceHandler<BluetoothDevice> {
  /**
   * A broadcast receiver that waits for {@link BluetoothDevice.ACTION_UUID} action intent and notifies a waiting thread.
   *
   * @author oli
   */
  final class Unlocker extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
      final String action = intent.getAction();
      if (BluetoothDevice.ACTION_UUID.equals(action)) {
        synchronized (BluetoothDeviceHandler.this) {
          // wakeup main thread
          BluetoothDeviceHandler.this.notify();
        }
      }
    }
  }

  /**
   * Get a list of all USB Devices currently attached in host mode.
   *
   * @param context
   * @return
   */
  public static Set<BluetoothDevice> getBluetoothDevices() {
    return adapter.getBondedDevices();
  }

  public static DeviceInfo<BluetoothDevice> getDeviceInfo(final BluetoothDevice device) {
    final DeviceInfo<BluetoothDevice> info = new DeviceInfo<BluetoothDevice>();

    info.setDevice(device);
    info.setName(device.getName());

    final String address = device.getAddress().replaceAll(":", "");
    info.setId(Long.parseLong(address, 16));

    return info;
  }

  /**
   * Does the mobile device have any USB devices attached in usb host mode?
   *
   * @param context
   * @return
   */
  public static boolean isBluetoothHost(final Context context) {
    return new BluetoothDeviceHandler(context).isBluetoothHost();
  }

  private static final String           UUID_SERIAL_PORT_PROFILE = "00001101-0000-1000-8000-00805F9B34FB";

  private List<UUID>                    uuids                    = null;

  private DeviceInfo<BluetoothDevice>   deviceInfo               = null;

  private SerialPort                    impl                     = null;

  private BluetoothSocket               socket                   = null;
  private static final BluetoothAdapter adapter                  = BluetoothAdapter.getDefaultAdapter();

  public BluetoothDeviceHandler(final Context context) {
    super(context);
  }

  @Override
  public void closeDevice() throws IOException {
    if (impl.isOpen()) {
      impl.close();
    }
    impl = null;

    if (socket != null) {
      socket.close();
      socket = null;
    }

  }

  @Override
  public DeviceAdapter<BluetoothDevice> getDeviceAdapter() {
    return new BluetoothDeviceAdapter(context);
  }

  @Override
  public DeviceInfo<BluetoothDevice> getDeviceInfo() {
    return deviceInfo;
  }

  @Override
  public SerialPort getPort() {
    return impl;
  }

  @Override
  public String getPreferenceKey() {
    return BluetoothDevice.class.getName();
  }

  /**
   * Check that this activity has permission to access the USB device and ask user for permission if necessary.
   *
   * @param device
   */
  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
  private List<UUID> getUUIDs() {
    if (uuids == null) {
      synchronized (this) {
        // setup broadcast receiver
        context.registerReceiver(new Unlocker(), new IntentFilter(BluetoothDevice.ACTION_UUID));

        getDevice().fetchUuidsWithSdp();

        try {
          // wait for user decision
          wait();
        } catch (final InterruptedException e) {
          // ignore
        }

        uuids = new ArrayList<UUID>();
        for (final ParcelUuid parcel : getDevice().getUuids()) {
          uuids.add(parcel.getUuid());
        }
      }
    }

    return uuids;
  }

  /**
   * Check if the device is USB host capable only once and remember the result.
   *
   * @return
   */
  public boolean isBluetoothHost() {
    return adapter != null && adapter.isEnabled() && !getBluetoothDevices().isEmpty();
  }

  @Override
  public void openDevice() throws IOException {
    if (deviceInfo != null) {
      socket = deviceInfo.getDevice().createRfcommSocketToServiceRecord(UUID.fromString(UUID_SERIAL_PORT_PROFILE));
      impl = new BluetoothSerialPortImplementation(socket);
    }
  }

  @Override
  public void setDevice(final BluetoothDevice device) {
    if (device == null) {
      deviceInfo = null;
    } else {
      deviceInfo = getDeviceInfo(device);
    }
  }
}