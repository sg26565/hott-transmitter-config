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
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import de.treichels.hott.ui.android.R;
import de.treichels.hott.ui.android.usb.SerialUsbDeviceAdapter;

/**
 * A {@link DialogFragment} that shows a list of model from the transmitter
 * memory and allows the user to select one of them.
 *
 * @author oli@treichels.de
 */
public class OpenFromMemoryDialog extends DialogFragment {
    private static final String  USB_DEVICE_NAME = "usbDeviceName";
    private DialogClosedListener closedListener  = null;
    private ModelInfo            info            = null;
    private ListView             listView;
    private SharedPreferences    preferences;
    private Spinner              spinner;
    private UsbDevice            usbDevice;

    public DialogClosedListener getDialogClosedListener() {
        return closedListener;
    }

    public ModelInfo getInfo() {
        return info;
    }

    public UsbDevice getUsbDevice() {
        return usbDevice;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        // inflate view from xml
        final View view = inflater.inflate(R.layout.load_from_tx, container);

        // generate a spinner for all USB devices attached via host mode
        final SerialUsbDeviceAdapter adapter = new SerialUsbDeviceAdapter(getActivity());
        spinner = (Spinner) view.findViewById(R.id.portSelector);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            /**
             * Handle selection of a device from the spinner.
             */
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                setUsbDevice((UsbDevice) parent.getItemAtPosition(position));
                listView.setAdapter(new ModelInfoAdapter(getActivity(), usbDevice));
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
            }
        });

        // initialize spinner to saved usb device name
        final String savedUsbDeviceName = preferences.getString(USB_DEVICE_NAME, null);
        if (savedUsbDeviceName != null) {
            final int position = adapter.getPosition(savedUsbDeviceName);
            if (position != -1) {
                spinner.setSelection(position);
            }
        }

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new ModelInfoAdapter(getActivity(), (UsbDevice) spinner.getItemAtPosition(0)));
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                setInfo((ModelInfo) parent.getItemAtPosition(position));
                dismiss();
                if (closedListener != null) {
                    closedListener.onDialogClosed(DialogClosedListener.OK);
                }
            }
        });

        getDialog().setTitle(R.string.action_load_from_tx);

        return view;
    }

    @Override
    public void onCancel(final DialogInterface dialog) {
        if (closedListener != null) {
            closedListener.onDialogClosed(DialogClosedListener.CANCELED);
        }
    }

    public void setDialogClosedListener(final DialogClosedListener closedListener) {
        this.closedListener = closedListener;
    }

    public void setInfo(final ModelInfo info) {
        this.info = info;
    }

    public void setUsbDevice(final UsbDevice usbDevice) {
        this.usbDevice = usbDevice;

        // save device name as preferences
        final SharedPreferences.Editor editor = preferences.edit();
        final String savedUsbDeviceName = usbDevice.getDeviceName();
        editor.putString(USB_DEVICE_NAME, savedUsbDeviceName);
        editor.commit();
    }
}