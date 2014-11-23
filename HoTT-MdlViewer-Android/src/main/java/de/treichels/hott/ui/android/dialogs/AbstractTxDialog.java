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
import android.widget.Spinner;
import android.widget.TextView;
import de.treichels.hott.ui.android.R;
import de.treichels.hott.ui.android.tx.DeviceAdapter;
import de.treichels.hott.ui.android.tx.DeviceHandler;

/**
 * An abstract dialog that provides a spinner to select a communication device and uses this device to retrieve a list of models to select from.
 *
 * @author oli
 */
public abstract class AbstractTxDialog<ResultType, DeviceType> extends DialogFragment implements OnItemClickListener {
  private DeviceAdapter<DeviceType> spinnerAdapter = null;
  private DialogClosedListener      closedListener = null;
  private ListView                  listView       = null;
  private TextView                  listViewLabel;
  private SharedPreferences         preferences;
  private ResultType                result         = null;
  private Spinner                   spinner        = null;

  /**
   * Listener to be notified when the dialog was closed.
   *
   * @return
   */
  public DialogClosedListener getDialogClosedListener() {
    return closedListener;
  }

  /**
   * The device handler for the dialog
   *
   * @return
   */
  public abstract DeviceHandler<DeviceType> getHandler();

  /**
   * An adapter that transforms the model data from the transmitter into widgets for a {@link ListView}.
   *
   * @return
   */
  protected abstract ListAdapter getListAdapter();

  /**
   * The label of the {@link ListView}.
   *
   * @return
   */
  protected abstract String getListViewLabel();

  /**
   * Load the preferred device from preferences. First, the preferred device name is read form the preferences and then the spinner adapter is used to find a
   * matching device. If no match was found, the first device will be used.
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  private DeviceType getPreferredDevice() {
    DeviceType device = null;

    final String savedDeviceName = preferences.getString(getHandler().getPreferenceKey(), null);
    if (savedDeviceName != null) {
      spinner.setSelection(spinnerAdapter.getPosition(savedDeviceName) != -1 ? spinnerAdapter.getPosition(savedDeviceName) : 0);
      device = (DeviceType) spinner.getSelectedItem();
    }

    return device;
  }

  /**
   * The model that was selected or null.
   *
   * @return
   */
  public ResultType getResult() {
    return result;
  }

  protected abstract int getSelectorLabelId();

  /**
   * A resource id for the title of the dialog.
   *
   * @return
   */
  protected abstract int getTitleId();

  /**
   * Notify the {@link DialogClosedListener} that the dialog was cancelled.
   *
   * @param dialog
   */
  @Override
  public void onCancel(final DialogInterface dialog) {
    if (getDialogClosedListener() != null) {
      getDialogClosedListener().onDialogClosed(DialogClosedListener.CANCELED);
    }
  }

  /**
   * Create the view for this dialog.
   *
   * @param inflater
   * @param container
   * @param savedInstanceState
   * @return
   */
  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

    // inflate view from xml
    final View view = inflater.inflate(R.layout.load_from_tx, container);

    // main list view
    listView = (ListView) view.findViewById(R.id.listView);
    listView.setEmptyView(view.findViewById(R.id.progressBar1));
    listView.setOnItemClickListener(this);

    listViewLabel = (TextView) view.findViewById(R.id.listViewLabel);
    listViewLabel.setText(getListViewLabel());

    // generate a spinner for communication devices
    spinnerAdapter = getHandler().getDeviceAdapter();
    spinner = (Spinner) view.findViewById(R.id.portSelector);
    spinner.setAdapter(spinnerAdapter);
    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      /**
       * Handle selection of a device from the spinner.
       */
      @SuppressWarnings("unchecked")
      @Override
      public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
        switchDevice((DeviceType) parent.getItemAtPosition(position));
      }

      @Override
      public void onNothingSelected(final AdapterView<?> parent) {
        // ignored
      }
    });

    // initialize spinner to saved device name
    switchDevice(getPreferredDevice());

    // set title
    getDialog().setTitle(getTitleId());

    return view;
  }

  private void savePreferredDevice(final DeviceType device) {
    // save device name as preferences
    final SharedPreferences.Editor editor = preferences.edit();
    editor.putString(getHandler().getPreferenceKey(), getHandler().getDeviceName());
    editor.commit();
  }

  public void setDialogClosedListener(final DialogClosedListener closedListener) {
    this.closedListener = closedListener;
  }

  protected void setListViewLabel(final String label) {
    listViewLabel.setText(label);
  }

  protected void setResult(final ResultType result) {
    this.result = result;
  }

  private void switchDevice(final DeviceType device) {
    if (device != null) {
      // switch to selected device
      getHandler().setDevice(device);

      // save as preference
      savePreferredDevice(device);

      // new list adapter for this device
      listView.setAdapter(getListAdapter());
    }
  }
}