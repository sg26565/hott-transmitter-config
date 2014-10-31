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

import gde.model.serial.SerialPort;

import java.io.IOException;
import java.util.List;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import de.treichels.hott.HoTTSerialPort;

/**
 * An abstract {@link AsyncTask} for USB communication.
 *
 * This class provides access to the {@link UsbManager} and asks the user for
 * device permissions.
 *
 * @author oli@treichels.de
 */
public abstract class UsbTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    final class Unlocker extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (mutex) {
                    // wakeup thread
                    mutex.notify();
                }
            }
        }
    }

    private static final String ACTION_USB_PERMISSION = "de.treichels.hott.ui.android.USB_PERMISSION";

    private final UsbDevice     device;
    private final UsbManager    manager;
    private final Object        mutex                 = new Object();
    protected final Context     context;
    protected HoTTSerialPort    port;


    public UsbTask(final Context context, final UsbDevice device) {
        this.context = context;
        this.device = device;
        manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }

    /**
     * Check that this activity has permission to access the usb device and ask
     * user for permission if necessary.
     *
     * @param device
     */
    protected boolean check4Permission() {
        if (!manager.hasPermission(device)) {
            synchronized (mutex) {
                final PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                final IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                context.registerReceiver(new Unlocker(), filter);
                manager.requestPermission(device, mPermissionIntent);

                try {
                    // wait for user decision
                    mutex.wait();
                } catch (final InterruptedException e) {
                    // ignore
                }
            }
        }

        return manager.hasPermission(device);
    }

    @SuppressWarnings("unchecked")
    protected abstract Result doInternal(Params... params) throws IOException;

    @SuppressWarnings("unchecked")
    @Override
    protected Result doInBackground(final Params... params) {
        check4Permission();
        final UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);

        if (driver != null) {
            final List<UsbSerialPort> ports = driver.getPorts();

            if (ports != null && ports.size() > 0) {
                final UsbDeviceConnection connection = manager.openDevice(device);
                final SerialPort impl = new AndroidUsbSerialPortImplementation(ports.get(0), connection);
                port = new HoTTSerialPort(impl);

                try {
                    port.open();
                    return doInternal(params);
                } catch (final IOException e) {
                    Log.e("doInBackground", "io error", e);
                } finally {
                    try {
                        port.close();
                        connection.close();
                    } catch (final IOException e) {
                        Log.e("doInBackground", "port_close_failed", e);
                    }
                }
            }
        }

        return null;
    }
}