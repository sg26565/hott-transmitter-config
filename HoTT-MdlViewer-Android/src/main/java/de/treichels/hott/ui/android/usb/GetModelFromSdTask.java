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

import gde.model.BaseModel;
import gde.model.HoTTException;
import gde.model.enums.ModelType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import de.treichels.hott.HoTTDecoder;
import de.treichels.hott.ui.android.R;


/**
 * Load the specified model from transmitter sd card.
 *
 * @author oli@treichels.de
 */
public class GetModelFromSdTask extends UsbTask<String, Void, BaseModel> {
    private static final String FILE_EXTENSION_MDL = ".mdl"; //$NON-NLS-1$

    /**
     * @param context
     */
    public GetModelFromSdTask(final Context context, final UsbDevice device) {
        super(context, device);
    }

    @Override
    protected BaseModel doInternal(final String... params) throws IOException {
        final String path = params[0];
        final String fileName = path.substring(path.lastIndexOf('/') + 1);

        // check file name extension (must be ".mdl")
        if (fileName == null || !fileName.endsWith(FILE_EXTENSION_MDL)) {
            throw new HoTTException(context.getResources().getString(R.string.msg_invalid_file_name, fileName));
        }

        // check model type (either 'a' or 'h')
        ModelType modelType;
        switch (fileName.charAt(0)) {
        case 'a':
            modelType = ModelType.Winged;
            break;

        case 'h':
            modelType = ModelType.Helicopter;
            break;

        default:
            throw new HoTTException(context.getResources().getString(R.string.msg_invalid_file_type, fileName.charAt(0)));
        }

        // filenName = modelType + modelName + ".mdl"
        final String modelName = fileName.substring(1, fileName.length() - 4);

        // read data
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        port.readFile(path, os);
        final ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

        // decode
        return HoTTDecoder.decodeStream(modelType, modelName, is);
    }
}
