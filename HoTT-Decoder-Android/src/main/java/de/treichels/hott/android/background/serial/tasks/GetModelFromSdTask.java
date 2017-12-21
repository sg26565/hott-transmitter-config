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
package de.treichels.hott.android.background.serial.tasks;

import de.treichels.hott.model.BaseModel;
import de.treichels.hott.model.enums.ModelType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.treichels.hott.HoTTDecoder;
import de.treichels.hott.android.background.serial.DeviceHandler;
import de.treichels.hott.android.background.serial.TxTask;
import de.treichels.hott.android.decoder.R;

/**
 * Load the specified model from transmitter sd card.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class GetModelFromSdTask extends TxTask<String, Void, BaseModel> {
  private static final String FILE_EXTENSION_MDL = ".mdl"; //$NON-NLS-1$

  public GetModelFromSdTask(final DeviceHandler<?> handler) {
    super(handler);
  }

  /**
   * Load the data of the model specified by the path in the first parameter. Other parameters are ignored.
   *
   * @param params
   *          First pram contains the file path on the sd card. Other params are ignored.
   * @return The model
   * @throws IOException
   */
  @Override
  protected BaseModel doInternal(final String... params) throws IOException {
    final String path = params[0];
    final String fileName = path.substring(path.lastIndexOf('/') + 1);

    // check file name extension (must be ".mdl")
    if (fileName == null || !fileName.endsWith(FILE_EXTENSION_MDL)) {
      setResultStatus(ResultStatus.error);
      setResultMessage(getContext().getResources().getString(R.string.msg_invalid_file_name, fileName));
      return null;
    }

    // check model type (either 'a' or 'h')
    final ModelType modelType=ModelType.forChar(fileName.charAt(0));

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
