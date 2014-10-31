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
package de.treichels.hott.ui.android.usb;

import gde.model.enums.ModelType;
import gde.model.serial.ModelInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.usb.UsbDevice;

/**
 * Retrieve a list of all model names from a transmitter attached via USB host
 * mode.
 *
 * @author oli@treichels.de
 */
public class GetAllModelsTask extends UsbTask<Void, ModelInfo, List<ModelInfo>> {
    /**
     * @param context
     */
    public GetAllModelsTask(final Context context, final UsbDevice device) {
        super(context, device);
    }

    @Override
    protected List<ModelInfo> doInternal(final Void... params) throws IOException {
        final List<ModelInfo> models = new ArrayList<ModelInfo>();

        final ModelInfo[] infos = port.getAllModelInfos();
        for (final ModelInfo info : infos) {
            if (info.getModelType() != ModelType.Unknown) {
                models.add(info);
                publishProgress(info);
            }
        }

        return models;
    }
}
