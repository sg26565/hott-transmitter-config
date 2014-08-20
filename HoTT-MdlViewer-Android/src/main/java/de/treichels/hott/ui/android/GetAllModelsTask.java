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
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import de.treichels.hott.HoTTSerialPort;

/**
 * Retrieve a list of all model names from a transmitter attached via USB host
 * mode.
 * 
 * @author oli
 */
class GetAllModelsTask extends UsbTask {
	private final List<String> models = new ArrayList<String>();

	/**
	 * @param context
	 */
	public GetAllModelsTask(final Context context) {
		super(context);
	}

	@Override
	@SuppressLint("DefaultLocale")
	protected List<String> doInBackground(final UsbDevice... params) {
		try {
			final UsbDevice device = params[0];

			check4Permission(device);

			final UsbSerialDriver driver = UsbSerialProber.getDefaultProber()
					.probeDevice(device);

			if (driver != null) {
				List<UsbSerialPort> ports = driver.getPorts();

				if (ports != null && ports.size() > 0) {
					final SerialPort impl = new AndroidUsbSerialPortImplementation(
							ports.get(0));
					final HoTTSerialPort port = new HoTTSerialPort(impl);
					port.open();
					final ModelInfo[] infos = port.getAllModelInfos();
					port.close();
					for (final ModelInfo info : infos) {
						if (info.getModelType() != ModelType.Unknown) {
							final String text = String.format("%2d: %s (%s)",
									info.getModelNumber(), info.getModelName(),
									info.getModelType());
							models.add(text);
							publishProgress(text);
						}
					}
				}
			}
		} catch (final IOException e) {
			Log.e("performUsbSearch", "getAllModelInfos", e);
			models.add(e.getMessage());
			for (final StackTraceElement element : e.getStackTrace()) {
				models.add(element.toString());
			}
		}

		return models;
	}
}