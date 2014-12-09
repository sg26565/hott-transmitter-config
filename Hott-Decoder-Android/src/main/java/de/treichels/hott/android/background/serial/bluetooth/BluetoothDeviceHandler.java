package de.treichels.hott.android.background.serial.bluetooth;

import gde.model.serial.SerialPort;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import de.treichels.hott.android.background.serial.DeviceAdapter;
import de.treichels.hott.android.background.serial.DeviceHandler;
import de.treichels.hott.android.background.serial.DeviceInfo;

/**
 * A {@link DeviceHandler} for {@link BluetoothDevice} objects.
 *
 * @author oli
 */
public class BluetoothDeviceHandler extends DeviceHandler<BluetoothDevice> {
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

  private static final BluetoothAdapter adapter                  = BluetoothAdapter.getDefaultAdapter();

  private static final String           UUID_SERIAL_PORT_PROFILE = "00001101-0000-1000-8000-00805F9B34FB";

  private DeviceInfo<BluetoothDevice>   deviceInfo               = null;

  private SerialPort                    impl                     = null;

  private BluetoothSocket               socket                   = null;

  public BluetoothDeviceHandler(final Context context) {
    super(context);
  }

  @Override
  public void closeDevice() throws IOException {
    if (impl != null && impl.isOpen()) {
      impl.close();
    }
    impl = null;

    if (socket != null) {
      socket.close();
    }
    socket = null;

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