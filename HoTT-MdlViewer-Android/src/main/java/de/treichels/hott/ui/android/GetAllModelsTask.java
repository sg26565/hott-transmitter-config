package de.treichels.hott.ui.android;

import gde.model.enums.ModelType;
import gde.model.serial.ModelInfo;
import gde.model.serial.SerialPort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.usb.UsbDevice;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import de.treichels.hott.HoTTSerialPort;
import de.treichels.hott.ui.android.AndroidUsbSerialPortImplementation;
import de.treichels.hott.ui.android.UsbTask;

/**
 * Retrieve a list of all model names from a transmitter attached via USB host mode.
 *
 * @author oli
 */
class GetAllModelsTask extends UsbTask<UsbDevice, ModelInfo, List<ModelInfo>> {
  private final List<ModelInfo> models = new ArrayList<ModelInfo>();

  /**
   * @param context
   */
  public GetAllModelsTask(final Context context) {
    super(context);
  }

  @Override
  @SuppressLint("DefaultLocale")
  protected List<ModelInfo> doInBackground(final UsbDevice... params) {
    try {
      final UsbDevice device = params[0];

      check4Permission(device);

      final UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);

      if (driver != null) {
        final List<UsbSerialPort> ports = driver.getPorts();

        if (ports != null && ports.size() > 0) {
          final SerialPort impl = new AndroidUsbSerialPortImplementation(ports.get(0));
          final HoTTSerialPort port = new HoTTSerialPort(impl);
          port.open();
          final ModelInfo[] infos = port.getAllModelInfos();
          port.close();
          for (final ModelInfo info : infos) {
            if (info.getModelType() != ModelType.Unknown) {
              models.add(info);
              publishProgress(info);
            }
          }
        }
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }

    return models;
  }
}
