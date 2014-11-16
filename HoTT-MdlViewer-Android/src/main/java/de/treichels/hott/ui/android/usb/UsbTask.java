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
import java.util.ArrayList;
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
import de.treichels.hott.ui.android.background.FailSafeAsyncTask;

/**
 * An abstract {@link AsyncTask} for USB communication.
 *
 * This class provides access to the {@link UsbManager} and asks the user for device permissions.
 *
 * @author oli@treichels.de
 */
public abstract class UsbTask<Params, Progress, Result> extends FailSafeAsyncTask<Params, Progress, Result> {
    /**
     * A broadcast receiver that waits for {@link UsbTask.ACTION_USB_PERMISSION} action intent and notifies a waiting thread.
     *
     * @author oli
     */
    final class Unlocker extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (UsbTask.this) {
                    // wakeup main thread
                    UsbTask.this.notify();
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
    public synchronized static List<UsbDevice> getDevices(final Context context) {
        final List<UsbDevice> list = new ArrayList<UsbDevice>();
        final UsbSerialProber prober = UsbSerialProber.getDefaultProber();

        // Check each device and filter on UsbSerial
        for (final UsbDevice device : getUsbManager(context).getDeviceList().values()) {
            final UsbSerialDriver driver = prober.probeDevice(device);
            if (driver != null) {
                list.add(device);
            }
        }

        return list;
    }

    /**
     * Get the system wide {@linkp UsbManager}.
     *
     * @param context
     * @return
     */
    public static UsbManager getUsbManager(final Context context) {
        if (manager == null) {
            manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        }

        return manager;
    }

    public UsbManager getUsbManager() {
        return getUsbManager(context);
    }

    /**
     * Check if the device is USB host capable only once and remember the result.
     *
     * @return
     */
    public static boolean isUsbHost(final Context context) {
        return !getDevices(context).isEmpty();
    }

    /** Constant for the USB permission action. */
    private static final String ACTION_USB_PERMISSION = "de.treichels.hott.ui.android.USB_PERMISSION";

    /** The current android contect */
    protected final Context     context;

    /** The current USB device for this task */
    private final UsbDevice     device;

    /** System wide USB manager service */
    private static UsbManager   manager               = null;

    /** {@link HoTTSerialPort} implementation for this task. */
    protected HoTTSerialPort    port;

    public UsbTask(final Context context, final UsbDevice device) {
        this.context = context;
        this.device = device;
    }

    /**
     * Check that this activity has permission to access the USB device and ask user for permission if necessary.
     *
     * @param device
     */
    protected boolean check4Permission() {
        if (!getUsbManager().hasPermission(device)) {
            synchronized (this) {
                // setup broadcast receiver
                context.registerReceiver(new Unlocker(), new IntentFilter(ACTION_USB_PERMISSION));

                // intent to fire on completion
                final PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);

                // ask for permission
                getUsbManager().requestPermission(device, permissionIntent);

                try {
                    // wait for user decision
                    wait();
                } catch (final InterruptedException e) {
                    // ignore
                }
            }
        }

        return getUsbManager().hasPermission(device);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Result doInBackground(final Params... params) {
        setResult(ResultStatus.running, null, null);
        Result result = null;

        check4Permission();
        final UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);

        if (driver != null) {
            final List<UsbSerialPort> ports = driver.getPorts();

            if (ports != null && ports.size() > 0) {
                final UsbDeviceConnection connection = getUsbManager().openDevice(device);
                final SerialPort impl = new AndroidUsbSerialPortImplementation(ports.get(0), connection);
                port = new HoTTSerialPort(impl);

                try {
                    port.open();
                    result = doInternal(params);
                } catch (final IOException e) {
                    Log.e("doInBackground", "io error", e);
                    setResult(ResultStatus.error, "io error", e);
                } finally {
                    try {
                        port.close();
                        connection.close();
                    } catch (final IOException e) {
                        Log.e("doInBackground", "port_close_failed", e);
                        setResult(ResultStatus.error, "io error", e);
                    }
                }
            }
        }

        setResult(ResultStatus.ok, null, null);
        return result;
    }

    @SuppressWarnings("unchecked")
    protected abstract Result doInternal(Params... params) throws IOException;
}