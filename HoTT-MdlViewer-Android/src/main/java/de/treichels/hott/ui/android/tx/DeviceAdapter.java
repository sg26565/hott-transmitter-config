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
package de.treichels.hott.ui.android.tx;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * An {@link BaseAdapter} that supports the display of a list of communication devices in a {@link Spinner}.
 *
 * @author oli
 */
public abstract class DeviceAdapter<DeviceType> extends BaseAdapter {
  protected final Context                      context;
  protected final List<DeviceInfo<DeviceType>> devices = new ArrayList<DeviceInfo<DeviceType>>();

  protected DeviceAdapter(final Context context) {
    this.context = context;
  }

  @Override
  public int getCount() {
    return getDevices().size();
  }

  protected List<DeviceInfo<DeviceType>> getDevices() {
    return devices;
  }

  @Override
  public Object getItem(final int position) {
    return getDevices().get(position).getDevice();
  }

  @Override
  public long getItemId(final int position) {
    return getDevices().get(position).getId();
  }

  /**
   * Get the position of the device in the list.
   *
   * @param device
   * @return
   */
  public int getPosition(final DeviceType device) {
    for (final DeviceInfo<DeviceType> info : getDevices()) {
      if (info.getDevice().equals(device)) {
        return getDevices().indexOf(info);
      }
    }

    return -1;
  }

  /**
   * Get the position of the device with the specified id in the list.
   *
   * @param deviceId
   * @return
   */
  public int getPosition(final long deviceId) {
    for (final DeviceInfo<DeviceType> info : getDevices()) {
      if (info.getId() == deviceId) {
        return getDevices().indexOf(info);
      }
    }

    return -1;
  }

  /**
   * Get the position of the device with the specified name in the list.
   *
   * @param deviceName
   * @return
   */
  public int getPosition(final String deviceName) {
    for (final DeviceInfo<DeviceType> info : getDevices()) {
      if (info.getName().equals(deviceName)) {
        return getDevices().indexOf(info);
      }
    }

    return -1;
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

    view.setText(getDevices().get(position).getName());

    return view;
  }
}