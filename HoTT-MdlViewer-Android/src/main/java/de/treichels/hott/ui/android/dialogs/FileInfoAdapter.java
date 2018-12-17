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

import de.treichels.hott.serial.FileInfo;
import de.treichels.hott.serial.FileType;
import android.content.Context;
import android.os.AsyncTask.Status;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import de.treichels.hott.android.background.serial.DeviceHandler;
import de.treichels.hott.android.background.serial.tasks.ListDirectoryTask;

/**
 * This class provides an adapter for {@link FileInfo} objects. It will list files and directories from the transmitter sd card and generate {@link View}
 * objects for use in a {@link ListView} for each of them.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class FileInfoAdapter extends GenericListAdaper<FileInfo> {
  private final DeviceHandler<?> handler;
  private ListDirectoryTask      task = null;

  /**
   * Create a FileInfoAdapter for the specified directory path.
   *
   * @param context
   * @param usbDevice
   */
  public FileInfoAdapter(final DeviceHandler<?> handler) {
    super(handler.getContext());
    this.handler = handler;
    reload("/");
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

    final FileInfo info = get(position);
    if (info != null) {
      switch (info.getType()) {
      case File:
        view.setText(info.getName());
        break;

      case Dir:
        view.setText(String.format("%s/", info.getName()));
        break;
      }
    }

    return view;
  }

  @Override
  public void reload() {
    reload("/");
  }

  public synchronized void reload(final String path) {
    // cancel running task
    if (task != null && task.getStatus() != Status.FINISHED) {
      task.cancel(true);
    }

    // Load all file infos from transmitter sd card as background task
    task = new ListDirectoryTask(handler) {
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
      protected void onProgressUpdate(final FileInfo... values) {
        // back to parent dir
        if (isEmpty() && !"/".equals(path)) {
          add(new FileInfo(path, "..", null, 0, null, FileType.Dir));
        }

        for (final FileInfo info : values) {
          add(info);
        }
      }
    };

    task.execute(path);
  }
}
