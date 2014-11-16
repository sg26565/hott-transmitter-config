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

import gde.model.BaseModel;

import java.io.IOException;

import de.treichels.hott.HoTTDecoder;

/**
 * Load the specified model from transmitter memory.
 *
 * @author oli@treichels.de
 */
public class GetModelFromMemoryTask extends TxTask<Integer, Void, BaseModel> {
  /**
   * @param context
   */
  public GetModelFromMemoryTask(final DeviceHandler<?> handler) {
    super(handler);
  }

  @Override
  protected BaseModel doInternal(final Integer... params) throws IOException {
    final int modelNumber = params[0];
    final BaseModel model = HoTTDecoder.decodeMemory(port, modelNumber);
    return model;
  }
}
