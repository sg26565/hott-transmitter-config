/**
 *  HoTT Transmitter Config
 *  Copyright (C) 2013  Oliver Treichel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
 * @author oli@treichels.de
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
