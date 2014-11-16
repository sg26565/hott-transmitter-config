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

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import de.treichels.hott.ui.android.R;
import de.treichels.hott.ui.android.tx.DeviceHandler;
import de.treichels.hott.ui.android.tx.usb.SerialUsbDeviceAdapter;

public abstract class AbstractTxDialog<ResultType, DeviceType> extends DialogFragment implements OnItemClickListener {
  private DialogClosedListener        closedListener = null;
  protected DeviceHandler<DeviceType> handler        = null;
  private TextView                    listViewLabel;
  private SharedPreferences           preferences;
  private ResultType                  result         = null;

  public DialogClosedListener getDialogClosedListener() {
    return closedListener;
  }

  public DeviceHandler<DeviceType> getHandler() {
    return handler;
  }

  protected abstract ListAdapter getListAdapter();

  protected abstract String getListViewLabel();

  public ResultType getResult() {
    return result;
  }

  protected abstract int getTitleId();

  @Override
  public void onCancel(final DialogInterface dialog) {
    if (getDialogClosedListener() != null) {
      getDialogClosedListener().onDialogClosed(DialogClosedListener.CANCELED);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

    // inflate view from xml
    final View view = inflater.inflate(R.layout.load_from_tx, container);

    final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);

    final ListView listView = (ListView) view.findViewById(R.id.listView);
    listView.setEmptyView(progressBar);
    listView.setOnItemClickListener(this);

    // generate a spinner for all USB devices attached via host mode
    final SerialUsbDeviceAdapter adapter = new SerialUsbDeviceAdapter(getActivity());
    final Spinner spinner = (Spinner) view.findViewById(R.id.portSelector);
    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      /**
       * Handle selection of a device from the spinner.
       */
      @Override
      public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
        savePreferences((DeviceType) parent.getItemAtPosition(position));
        // switch to selected device
        listView.setAdapter(getListAdapter());
      }

      @Override
      public void onNothingSelected(final AdapterView<?> parent) {}
    });

    // initialize spinner to saved usb device name
    final String savedDeviceId = preferences.getString(handler.getClass().getSimpleName(), null);
    if (savedDeviceId != null) {
      final int position = adapter.getPosition(savedDeviceId);
      spinner.setSelection(position != -1 ? position : 0);
      savePreferences((DeviceType) spinner.getSelectedItem());
      listView.setAdapter(getListAdapter());
    }

    getDialog().setTitle(getTitleId());

    listViewLabel = (TextView) view.findViewById(R.id.listViewLabel);
    listViewLabel.setText(getListViewLabel());

    return view;
  }

  private void savePreferences(final DeviceType device) {
    setHandler(device);

    // save device name as preferences
    final SharedPreferences.Editor editor = preferences.edit();
    final String savedDeviceId = getHandler().getDeviceId();
    editor.putString(device.getClass().getSimpleName(), savedDeviceId);
    editor.commit();
  }

  public void setDialogClosedListener(final DialogClosedListener closedListener) {
    this.closedListener = closedListener;
  }

  protected abstract void setHandler(DeviceType device);

  protected void setLabel(final String label) {
    listViewLabel.setText(label);
  }

  protected void setResult(final ResultType result) {
    this.result = result;
  }
}