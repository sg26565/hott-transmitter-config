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
package de.treichels.hott.ui.android.tx;

import java.io.IOException;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import de.treichels.hott.HoTTSerialPort;
import de.treichels.hott.ui.android.background.FailSafeAsyncTask;

/**
 * An abstract {@link AsyncTask} for USB communication.
 *
 * This class provides access to the {@link UsbManager} and asks the user for device permissions.
 *
 * @author oli@treichels.de
 */
public abstract class TxTask<Params, Progress, Result> extends FailSafeAsyncTask<Params, Progress, Result> {
  private final DeviceHandler<?> handler;
  /** {@link HoTTSerialPort} implementation for this task. */
  protected HoTTSerialPort       port;

  public TxTask(final DeviceHandler<?> handler) {
    this.handler = handler;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Result doInBackgroundFailSafe(final Params... params) throws IOException {
    Result result = null;

    handler.openDevice();
    port = new HoTTSerialPort(handler.getPort());

    try {
      port.open();
      result = doInternal(params);
    } finally {
      port.close();
      handler.closeDevice();
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  protected abstract Result doInternal(Params... params) throws IOException;

  protected Context getContext() {
    return handler.getContext();
  }
}