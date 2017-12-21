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
package de.treichels.hott.ui.android.dialogs;

import de.treichels.hott.model.serial.ModelInfo;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import de.treichels.hott.android.background.serial.DeviceHandler;
import de.treichels.hott.android.background.serial.tasks.GetAllModelsTask;

/**
 * This class provides an adapter for {@link ModelInfo} instances. It will load all defined models from the transmitter memory and generate {@link View} objects
 * for use in a {@link ListView} for each model.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class ModelInfoAdapter extends GenericListAdaper<ModelInfo> {
  private final DeviceHandler<?> handler;
  private GetAllModelsTask       task = null;

  /**
   * @param context
   * @param usbDevice
   */
  public ModelInfoAdapter(final DeviceHandler<?> handler) {
    super(handler.getContext());
    this.handler = handler;
  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent) {
    final TextView view;

    if (convertView == null) {
      final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
    } else {
      view = (TextView) convertView;
    }

    final ModelInfo info = get(position);
    if (info != null) {
      view.setText(String.format("%2d: %s (%s)", info.getModelNumber() + 1, info.getModelName(), info.getModelType()));
    }

    return view;
  }

  @Override
  public synchronized void reload() {
    if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
      task.cancel(true);
    }

    // Load all model infos from transmitter memory as background task
    task = new GetAllModelsTask(handler) {
      @Override
      protected void onError(final String message, final Throwable throwable) {
        if (isEmpty()) {
          // stop progressbar
          add(null);
        }
        showDialog(message);
      }

      @Override
      protected void onPreExecute() {
        // remove old entries
        clear();
      }

      @Override
      protected void onProgressUpdate(final ModelInfo... values) {
        for (final ModelInfo info : values) {
          add(info);
        }
      }
    };

    task.execute();
  }
}
