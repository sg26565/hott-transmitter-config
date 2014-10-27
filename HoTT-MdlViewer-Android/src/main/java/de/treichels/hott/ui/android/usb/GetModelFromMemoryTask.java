package de.treichels.hott.ui.android.usb;

import gde.model.BaseModel;
import gde.model.serial.SerialPort;

import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import de.treichels.hott.HoTTDecoder;
import de.treichels.hott.HoTTSerialPort;

/**
 * Load the specified model from transmitter memory.
 *
 * @author oli
 */
public class GetModelFromMemoryTask extends UsbTask<Integer, Void, BaseModel> {
    private final UsbDevice device;

    /**
     * @param context
     */
    public GetModelFromMemoryTask(final Context context, final UsbDevice device) {
        super(context);
        this.device = device;
    }

    @Override
    @SuppressLint("DefaultLocale")
    protected BaseModel doInBackground(final Integer... params) {
        final int modelNumber = params[0];
        BaseModel model = null;

        check4Permission(device);
        final UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);

        if (driver != null) {
            final List<UsbSerialPort> ports = driver.getPorts();

            if (ports != null && ports.size() > 0) {
                final UsbDeviceConnection connection = manager.openDevice(device);
                final SerialPort impl = new AndroidUsbSerialPortImplementation(ports.get(0), connection);
                final HoTTSerialPort port = new HoTTSerialPort(impl);

                try {
                    port.open();
                    model = HoTTDecoder.decodeMemory(port, modelNumber);
                    port.close();
                } catch (final IOException e) {
                }
            }
        }

        return model;
    }
}
