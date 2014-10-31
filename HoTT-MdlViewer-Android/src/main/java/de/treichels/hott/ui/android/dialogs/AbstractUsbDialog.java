package de.treichels.hott.ui.android.dialogs;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import de.treichels.hott.ui.android.R;
import de.treichels.hott.ui.android.usb.SerialUsbDeviceAdapter;

public abstract class AbstractUsbDialog<T> extends DialogFragment implements OnItemClickListener {
    private static final String  USB_DEVICE_NAME = "usbDeviceName";
    private DialogClosedListener closedListener  = null;
    private SharedPreferences    preferences;
    private T                    result          = null;
    private UsbDevice            usbDevice;
    private TextView             listViewLabel;

    public T getResult() {
        return result;
    }

    protected void setResult(final T result) {
        this.result = result;
    }

    public DialogClosedListener getDialogClosedListener() {
        return closedListener;
    }

    protected abstract ListAdapter getListAdapter();

    public UsbDevice getUsbDevice() {
        return usbDevice;
    }

    @Override
    public void onCancel(final DialogInterface dialog) {
        if (closedListener != null) {
            closedListener.onDialogClosed(DialogClosedListener.CANCELED);
        }
    }

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
                setUsbDevice((UsbDevice) parent.getItemAtPosition(position));
                // switch to selected device
                listView.setAdapter(getListAdapter());
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
            }
        });

        // initialize spinner to saved usb device name
        final String savedUsbDeviceName = preferences.getString(USB_DEVICE_NAME, null);
        if (savedUsbDeviceName != null) {
            final int position = adapter.getPosition(savedUsbDeviceName);
            spinner.setSelection(position != -1 ? position : 0);
            setUsbDevice((UsbDevice) spinner.getSelectedItem());
            listView.setAdapter(getListAdapter());
        }

        getDialog().setTitle(getTitleId());

        listViewLabel = (TextView) view.findViewById(R.id.listViewLabel);
        listViewLabel.setText(getListViewLabel());

        return view;
    }

    protected void setListViewLabel(final String label) {
        listViewLabel.setText(label);
    }

    protected abstract int getTitleId();

    protected abstract String getListViewLabel();

    public void setDialogClosedListener(final DialogClosedListener closedListener) {
        this.closedListener = closedListener;
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