/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package de.treichels.hott.serial

import de.treichels.hott.model.enums.ModelType
import de.treichels.hott.model.enums.ReceiverClass
import de.treichels.hott.model.enums.TransmitterType

import java.io.Serializable

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
data class ModelInfo(
        val modelNumber: Int,
        val modelName: String,
        val modelInfo: String,
        val modelType: ModelType,
        val transmitterType: TransmitterType=TransmitterType.Unknown,
        val receiverClass: ReceiverClass = ReceiverClass.Unknown,
        val usedHours: Int = 0,
        val usedMinutes: Int = 0
) : Serializable {
    override fun toString(): String {
        return String.format(
                "ModelInfo [modelNumber=%s, modelName=%s, modelInfo=%s, modelType=%s, transmitterType=%s, receiverClass=%s, usedHours=%s, usedMinutes=%s]",
                modelNumber, modelName, modelInfo, modelType, transmitterType, receiverClass, usedHours, usedMinutes)
    }
}
