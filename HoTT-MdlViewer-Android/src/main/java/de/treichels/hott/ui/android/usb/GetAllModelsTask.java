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

import gde.model.enums.ModelType;
import gde.model.serial.ModelInfo;
import gde.model.serial.SerialPort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import de.treichels.hott.HoTTSerialPort;

/**
 * Retrieve a list of all model names from a transmitter attached via USB host
 * mode.
 *
 * @author oli@treichels.de
 */
public class GetAllModelsTask extends UsbTask<UsbDevice, ModelInfo, List<ModelInfo>> {
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
        final UsbDevice device = params[0];

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
                    final ModelInfo[] infos = port.getAllModelInfos();
                    port.close();

                    for (final ModelInfo info : infos) {
                        if (info.getModelType() != ModelType.Unknown) {
                            models.add(info);
                            publishProgress(info);
                        }
                    }
                } catch (final IOException e) {
                }
            }
        }

        return models;
    }
}
