package de.treichels.hott.ui.android;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;

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

  protected final UsbManager  manager;
  protected final Context     context;
  private final Object        mutex                 = new Object();

  public UsbTask(final Context context) {
    this.context = context;
    manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
  }

  /**
   * Check that this activity has permission to access the usb device and ask user for permission if necessary.
   * 
   * @param device
   */
  protected boolean check4Permission(final UsbDevice device) {
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
}