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

import de.treichels.hott.model.enums.LCDType
import de.treichels.hott.model.enums.TransmitterType
import java.io.Serializable

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
data class TxInfo(
        val rfId: Long,
        val transmitterType: TransmitterType,
        val appVersion: Int,
        val memoryVersion: Int,
        val bootProductCode: Int,
        val bootVersion: Int,
        val lcdType: LCDType,
        val transmitterName: String,
        val vendorName: String,
        val ownerName: String,
        val customPhaseNames: List<String>) : Serializable {
    override fun toString(): String {
        return "TxInfo [transmitterType=$transmitterType, appVersion=$appVersion, memoryVersion=$memoryVersion, bootProductCode=$bootProductCode, bootVersion=$bootVersion, lcdType=$lcdType transmitterName=$transmitterName, vendorName=$vendorName, ownerName=$ownerName, customPhaseNames=$customPhaseNames]"
    }

}
