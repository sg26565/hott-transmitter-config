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
package de.treichels.hott.android.background.serial;

import android.content.Context;
import de.treichels.hott.HoTTSerialPort;
import de.treichels.hott.android.background.FailSafeAsyncTask;

import java.io.IOException;

/**
 * An abstract {@link FailSafeAsyncTask} for communication with the transmitter that is independent form the communication channel (Usb or Bluetooth).
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public abstract class TxTask<Params, Progress, Result> extends FailSafeAsyncTask<Params, Progress, Result> {
  private final DeviceHandler<?> handler;
  /** {@link HoTTSerialPort} implementation for this task. */
  protected HoTTSerialPort       port;

  public TxTask(final DeviceHandler<?> handler) {
    this.handler = handler;
  }

  /**
   * Perform communication in a device indepentent way. Ask the handler to setup and open the device, open a communication port, perform the task and close the
   * port and device.
   *
   * @param params
   * @return
   * @throws IOException
   */
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

  /**
   * Perform the task. Sub classes can rely on communication port being ready for use.
   * 
   * @param params
   * @return
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  protected abstract Result doInternal(Params... params) throws IOException;

  protected Context getContext() {
    return handler.getContext();
  }
}
