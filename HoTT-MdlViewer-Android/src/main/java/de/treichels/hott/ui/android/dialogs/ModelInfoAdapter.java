package de.treichels.hott.ui.android.dialogs;

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
import gde.model.serial.ModelInfo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.treichels.hott.ui.android.usb.GetAllModelsTask;

/**
 * This class provides an adapter for {@link ModelInfo} instances. It will load
 * all defined models from the transmitter memory and generate {@link View}
 * objects for use in a {@link ListView} for each model.
 *
 * @author oli@treichels.de
 */
public class ModelInfoAdapter extends BaseAdapter {
    private final Context         context;
    private final List<ModelInfo> modelInfos;

    /**
     * @param usbDevice
     */
    public ModelInfoAdapter(final Context context, final UsbDevice usbDevice) {
        this.context = context;
        modelInfos = new ArrayList<ModelInfo>();

        // Load all model infos from transmitter memory as background task
        new GetAllModelsTask(context) {
            @Override
            protected void onProgressUpdate(final ModelInfo... values) {
                for (final ModelInfo info : values) {
                    modelInfos.add(info);
                }
                notifyDataSetChanged();
            }
        }.execute(usbDevice);
    }

    @Override
    public int getCount() {
        return modelInfos.size();
    }

    @Override
    public Object getItem(final int position) {
        return modelInfos.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final TextView view;

        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        } else {
            view = (TextView) convertView;
        }

        final ModelInfo info = modelInfos.get(position);
        view.setText(String.format("%2d: %s (%s)", info.getModelNumber() + 1, info.getModelName(), info.getModelType()));

        return view;
    }
}
