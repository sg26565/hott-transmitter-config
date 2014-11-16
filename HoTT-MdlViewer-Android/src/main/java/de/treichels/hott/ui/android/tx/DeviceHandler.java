package de.treichels.hott.ui.android.tx;

import gde.model.serial.SerialPort;

import java.io.IOException;

import android.content.Context;

public abstract class DeviceHandler<DeviceType> {
  protected final Context    context;
  protected final DeviceType device;

  public DeviceHandler(final Context context, final DeviceType device) {
    this.context = context;
    this.device = device;
  }

  public abstract void closeDevice() throws IOException;

  public Context getContext() {
    return context;
  }

  public DeviceType getDevice() {
    return device;
  }

  public abstract String getDeviceId();

  public abstract SerialPort getPort();

  public abstract void openDevice();
}
