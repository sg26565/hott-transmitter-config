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

import gde.model.enums.ModelType;
import gde.model.serial.ModelInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.treichels.hott.android.background.serial.DeviceHandler;
import de.treichels.hott.android.background.serial.TxTask;

/**
 * Retrieve a list of all model names from the transmitter.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class GetAllModelsTask extends TxTask<Void, ModelInfo, List<ModelInfo>> {
  public GetAllModelsTask(final DeviceHandler<?> handler) {
    super(handler);
  }

  /**
   * Get a list of all model infos from the transmitter memory.
   *
   * @param params
   *          ignored
   * @return A list of {@link ModelInfo} objects.
   * @throws IOException
   */
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
