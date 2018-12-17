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
import android.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;

/**
 * A {@link DialogFragment} that shows a list of files from the transmitter sd card and allows the user to select one of them.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public abstract class OpenFromSdDialog<DeviceType> extends AbstractTxDialog<String, DeviceType> {
  private FileInfoAdapter listAdapter = null;

  @Override
  protected GenericListAdaper<FileInfo> getListViewAdapter() {
    if (listAdapter == null) {
      // this dialog displays a list of file infos
      listAdapter = new FileInfoAdapter(getDeviceHandler());
    }

    return listAdapter;
  }

  @Override
  protected String getListViewLabel() {
    // display the current file path as the list view label
    return getResult();
  }

  @Override
  public String getResult() {
    String result = super.getResult();

    if (result == null || result.length() == 0) {
      result = "/";
    }

    return result;
  }

  @Override
  public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
    final FileInfo item = (FileInfo) parent.getItemAtPosition(position);

    // handle parent references (i.e. replace "/abc/.." with "")
    final String result = item.getPath().replaceAll("/[^/]*/\\.\\.", "");

    setResult(result);
    setListViewLabel(getResult());

    if (item.getType() == FileType.File) {
      // file selected - close dialog and notify listener
      dismiss();

      final DialogClosedListener listener = getDialogClosedListener();
      if (listener != null) {
        listener.onDialogClosed(DialogClosedListener.OK);
      }
    } else {
      // directory selected - open directory
      listAdapter.reload(getResult());
    }
  }
}