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

import gde.model.serial.FileInfo;
import gde.model.serial.FileType;
import android.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import de.treichels.hott.ui.android.R;

/**
 * A {@link DialogFragment} that shows a list of files from the transmitter sd card and allows the user to select one of them.
 *
 * @author oli@treichels.de
 */
public abstract class OpenFromSdDialog<DeviceType> extends AbstractTxDialog<String, DeviceType> {
  private FileInfoAdapter adapter = null;

  @Override
  protected ListAdapter getListAdapter() {
    if (adapter == null) {
      adapter = new FileInfoAdapter(getHandler());
    }

    return adapter;
  }

  @Override
  protected String getListViewLabel() {
    return getResult();
  }

  @Override
  public String getResult() {
    final String result = super.getResult();

    if (result == null || result.length() == 0) {
      return "/";
    }

    return result;
  }

  @Override
  protected int getTitleId() {
    return R.string.action_load_from_sd;
  }

  @Override
  public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
    final FileInfo item = (FileInfo) parent.getItemAtPosition(position);
    final String result = item.getPath().replaceAll("/[^/]*/\\.\\.", "");
    setResult(result);
    setLabel(getResult());

    if (item.getType() == FileType.File) {
      // file selected - close dialog and notify listener
      dismiss();

      final DialogClosedListener listener = getDialogClosedListener();
      if (listener != null) {
        listener.onDialogClosed(DialogClosedListener.OK);
      }
    } else {
      // directory selected - open directory
      adapter.reload(getResult());
    }
  }
}