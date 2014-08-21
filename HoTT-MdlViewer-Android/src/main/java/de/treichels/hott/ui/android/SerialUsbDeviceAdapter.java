package de.treichels.hott.ui.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SerialUsbDeviceAdapter extends BaseAdapter {
  private final UsbManager                 manager;
  private final HashMap<String, UsbDevice> usbDevices;
  private final List<String>               usbDeviceNames;
  private final Context                    context;

  public SerialUsbDeviceAdapter(final Context context) {
    this.context = context;
    manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    usbDevices = manager.getDeviceList();
    usbDeviceNames = new ArrayList<>(usbDevices.keySet());
  }

  @Override
  public int getCount() {
    return usbDevices.size();
  }

  @Override
  public Object getItem(final int position) {
    return usbDevices.get(usbDeviceNames.get(position));
  }

  @Override
  public long getItemId(final int position) {
    return position;
  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent) {
    final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final TextView view = (TextView) inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent);
    view.setText(usbDeviceNames.get(position));
    return view;
  }
}
