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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import de.treichels.hott.android.background.serial.DeviceAdapter;
import de.treichels.hott.android.background.serial.DeviceHandler;
import de.treichels.hott.ui.android.R;

/**
 * An abstract dialog that provides a spinner to select a communication device and uses this device to retrieve a list of models to select from.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public abstract class AbstractTxDialog<ResultType, DeviceType> extends DialogFragment implements OnItemClickListener {
  private DialogClosedListener      closedListener      = null;
  private ListView                  listView            = null;
  private TextView                  listViewLabel       = null;
  private Spinner                   portSelector        = null;
  private DeviceAdapter<DeviceType> portSelectorAdapter = null;
  private SharedPreferences         preferences         = null;
  private ResultType                result              = null;

  /**
   * The device handler for the dialog
   *
   * @return
   */
  public abstract DeviceHandler<DeviceType> getDeviceHandler();

  /**
   * Listener to be notified when the dialog was closed.
   *
   * @return
   */
  public DialogClosedListener getDialogClosedListener() {
    return closedListener;
  }

  /**
   * An adapter that transforms the model data from the transmitter into widgets for a {@link ListView}.
   *
   * @return
   */
  protected abstract GenericListAdaper<?> getListViewAdapter();

  /**
   * The label text of the {@link ListView}.
   *
   * @return
   */
  protected abstract String getListViewLabel();

  /**
   * The label text for the port selection {@link Spinner}.
   *
   * @return
   */
  protected abstract int getPortSelectorLabelId();

  /**
   * The model that was selected or null.
   *
   * @return
   */
  public ResultType getResult() {
    return result;
  }

  /**
   * A resource id for the title of the dialog.
   *
   * @return
   */
  protected abstract int getTitleId();

  /**
   * Load the preferred device from preferences. First, the preferred device name is read form the preferences and then the spinner adapter is used to find a
   * matching device. If no match was found, the first device will be used.
   *
   * @return
   */
  private void loadPreferredDevice() {

    final String savedDeviceName = preferences.getString(getDeviceHandler().getPreferenceKey(), null);
    if (savedDeviceName != null) {
      // this will trigger onItemSelected()
      portSelector.setSelection(portSelectorAdapter.getPosition(savedDeviceName) != -1 ? portSelectorAdapter.getPosition(savedDeviceName) : 0);
    }
  }

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

    // set title
    getDialog().setTitle(getTitleId());

    // inflate view from xml
    final View view = inflater.inflate(R.layout.load_from_tx, container);

    // generate a spinner for communication devices
    final TextView portSelectorLabel = (TextView) view.findViewById(R.id.portSelectorLabel);
    portSelectorLabel.setText(getPortSelectorLabelId());

    portSelectorAdapter = getDeviceHandler().getDeviceAdapter();
    portSelector = (Spinner) view.findViewById(R.id.portSelector);
    portSelector.setAdapter(portSelectorAdapter);
    portSelector.setOnItemSelectedListener(new OnItemSelectedListener() {
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
        Toast.makeText(getActivity(), R.string.msg_select_device, Toast.LENGTH_LONG).show();
      }
    });

    // main list view
    listViewLabel = (TextView) view.findViewById(R.id.listViewLabel);
    listViewLabel.setText(getListViewLabel());

    listView = (ListView) view.findViewById(R.id.listView);
    listView.setEmptyView(view.findViewById(R.id.progressBar1));
    listView.setOnItemClickListener(this);

    // initialize spinner to saved device name
    loadPreferredDevice();

    return view;
  }

  /**
   * Save the selected device for re-use on next dialog invocaton.
   *
   * @param device
   */
  private void savePreferredDevice(final DeviceType device) {
    // save device name as preferences
    final SharedPreferences.Editor editor = preferences.edit();
    editor.putString(getDeviceHandler().getPreferenceKey(), getDeviceHandler().getDeviceName());
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

  /**
   * Switch to the specificed device.
   *
   * @param device
   */
  private void switchDevice(final DeviceType device) {
    Log.d("switchDevice", "enter");

    if (device != null) {
      // switch to selected device
      getDeviceHandler().setDevice(device);

      // save as preference
      savePreferredDevice(device);

      // new list adapter for this device
      final GenericListAdaper<?> adapter = getListViewAdapter();
      listView.setAdapter(adapter);
      adapter.clear();
      adapter.reload();
    }
  }
}