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
package gde.model.serial;

import gde.model.enums.ModelType;
import gde.model.enums.TransmitterType;

import java.io.Serializable;

/**
 * @author oli
 * 
 */
public class ModelInfo implements Serializable {
  private static final long     serialVersionUID = 1L;

  private final int             modelNumber;
  private final String          modelName;
  private final ModelType       modelType;
  private final TransmitterType transmitterType;

  /**
   * @param modelNumber
   * @param modelName
   * @param modelType
   */
  public ModelInfo(final int modelNumber, final String modelName, final ModelType modelType, final TransmitterType transmitterType) {
    this.modelNumber = modelNumber;
    this.modelName = modelName;
    this.modelType = modelType;
    this.transmitterType = transmitterType;
  }

  public String getModelName() {
    return modelName;
  }

  public int getModelNumber() {
    return modelNumber;
  }

  public ModelType getModelType() {
    return modelType;
  }

  public TransmitterType getTransmitterType() {
    return transmitterType;
  }
}