/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package gde.model.serial;

import java.io.Serializable;

import gde.model.enums.ModelType;
import gde.model.enums.ReceiverType;
import gde.model.enums.TransmitterType;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public class ModelInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int modelNumber;
    private final String modelName;
    private final String modelInfo;
    private final ModelType modelType;
    private final TransmitterType transmitterType;
    private final ReceiverType receiverType;
    private final int usedHours;
    private final int usedMinutes;

    public ModelInfo(final int modelNumber, final String modelName, final ModelType modelType, final TransmitterType transmitterType,
            final ReceiverType receiverType) {
        this(modelNumber, modelName, null, modelType, transmitterType, receiverType, 0, 0);
    }

    public ModelInfo(final int modelNumber, final String modelName, final String modelInfo, final ModelType modelType, final TransmitterType transmitterType,
            final ReceiverType receiverType, final int usedHours, final int usedMinutes) {
        this.modelNumber = modelNumber;
        this.modelName = modelName;
        this.modelInfo = modelInfo;
        this.modelType = modelType;
        this.transmitterType = transmitterType;
        this.receiverType = receiverType;
        this.usedHours = usedHours;
        this.usedMinutes = usedMinutes;
    }

    public String getModelInfo() {
        return modelInfo;
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

    public ReceiverType getReceiverType() {
        return receiverType;
    }

    public TransmitterType getTransmitterType() {
        return transmitterType;
    }

    public int getUsedHours() {
        return usedHours;
    }

    public int getUsedMinutes() {
        return usedMinutes;
    }

    @Override
    public String toString() {
        return String.format(
                "ModelInfo [modelNumber=%s, modelName=%s, modelInfo=%s, modelType=%s, transmitterType=%s, receiverType=%s, usedHours=%s, usedMinutes=%s]", //$NON-NLS-1$
                modelNumber, modelName, modelInfo, modelType, transmitterType, receiverType, usedHours, usedMinutes);
    }
}
