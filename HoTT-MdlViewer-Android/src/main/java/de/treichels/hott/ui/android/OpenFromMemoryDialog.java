package de.treichels.hott.ui.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;

public class OpenFromMemoryDialog extends DialogFragment {
  private Spinner   spinner;
  private ListView  listView;
  private UsbDevice usbDevice;

  @Override
  public Dialog onCreateDialog(final Bundle savedInstanceState) {
    final LayoutInflater inflater = getActivity().getLayoutInflater();
    final View view = inflater.inflate(R.layout.load_from_tx, null);

    spinner = (Spinner) view.findViewById(R.id.portSelector);
    spinner.setAdapter(new SerialUsbDeviceAdapter(getActivity()));
    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
        usbDevice = (UsbDevice) parent.getItemAtPosition(position);
        listView.setAdapter(new ModelInfoAdapter(getActivity(), usbDevice));
      }

      @Override
      public void onNothingSelected(final AdapterView<?> parent) {}
    });

    listView = (ListView) view.findViewById(R.id.listView);
    listView.setAdapter(new ModelInfoAdapter(getActivity(), (UsbDevice) spinner.getItemAtPosition(0)));
    listView.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
        // TODO Load model
      }

      @Override
      public void onNothingSelected(final AdapterView<?> parent) {}
    });

    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.action_load_from_tx);
    builder.setView(view);

    return builder.create();
  }
}