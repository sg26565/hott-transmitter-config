package de.treichels.hott.ui.android.dialogs;

import gde.model.serial.ModelInfo;
import android.app.DialogFragment;
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

public class OpenFromMemoryDialog extends DialogFragment {
  private ModelInfo info = null;
  private Spinner   spinner;
  private ListView  listView;
  private UsbDevice usbDevice;

  public ModelInfo getInfo() {
    return info;
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.load_from_tx, container);

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
    listView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        setInfo((ModelInfo) parent.getItemAtPosition(position));
        dismiss();
      }
    });

    getDialog().setTitle(R.string.action_load_from_tx);

    return view;
  }

  public void setInfo(final ModelInfo info) {
    this.info = info;
  }
}