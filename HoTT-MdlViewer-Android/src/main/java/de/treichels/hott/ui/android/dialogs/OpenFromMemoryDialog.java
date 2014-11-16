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

import gde.model.serial.ModelInfo;
import android.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import de.treichels.hott.ui.android.R;

/**
 * A {@link DialogFragment} that shows a list of models from the transmitter memory and allows the user to select one of them.
 *
 * @author oli@treichels.de
 */
public abstract class OpenFromMemoryDialog<DeviceType> extends AbstractTxDialog<ModelInfo, DeviceType> {
  private ListAdapter adapter = null;

  @Override
  protected ListAdapter getListAdapter() {
    if (adapter == null) {
      adapter = new ModelInfoAdapter(getHandler());
    }

    return adapter;
  }

  @Override
  protected String getListViewLabel() {
    return getActivity().getString(R.string.model_memory);
  }

  @Override
  protected int getTitleId() {
    return R.string.action_load_from_tx;
  }

  @Override
  public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
    setResult((ModelInfo) parent.getItemAtPosition(position));

    dismiss();

    final DialogClosedListener listener = getDialogClosedListener();
    if (listener != null) {
      listener.onDialogClosed(DialogClosedListener.OK);
    }
  }
}