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
package de.treichels.hott.ui.android.tx.usb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * An {@link Adapter} that holds a list of UsbDevices.
 *
 * @author oli@treichels.de
 */
public class SerialUsbDeviceAdapter extends BaseAdapter {
    private final Context                    context;
    private final UsbManager                 manager;
    private final List<String>               usbDeviceNames;
    private final HashMap<String, UsbDevice> usbDevices;

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

    public int getPosition(final String deviceName) {
        return usbDeviceNames.indexOf(deviceName);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final TextView view;

        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (TextView) inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        } else {
            view = (TextView) convertView;
        }

        view.setText(usbDeviceNames.get(position));

        return view;
    }
}
