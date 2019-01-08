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
package de.treichels.hott.model.enums

import de.treichels.hott.util.get
import java.util.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
enum class SensorType(override val productCode: Int = 0, override val orderNo: String = "", override val category: String = SensorType.category) : Registered<SensorType> {
    Receiver,
    GeneralModule,
    ElectricAirModule(13015030, "33620"),
    Vario(13015000, "33601"),
    GPS(13015070, "S8437"),
    ESC,
    GeneralEngineModule(13015020, "33610"),
    GeneralAirModule(13015020, "33611"),
    GPS_OLD(13015040, "33600"),
    VM_60V(13018600, "S8389"),
    VM_22V_SBEC_GER(13019600, "S8446"),
    VM_22V_SBEC_USA(13019601, "S8446"),
    VM_10V(13019602, "S8446.1"),
    PDB(13020000, "S8474"),
    None;

    val map = 1 shl ordinal and 0b111111

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name] + if (orderNo.isNotEmpty()) " ($orderNo)" else ""

    companion object {
        fun forMap(map: Int): List<SensorType> = SensorType.values().filter { s -> s.ordinal < 6 && s.map and map != 0 }.toList()
        fun getMap(sensors: List<SensorType>): Int = sensors.stream().mapToInt { it.map }.sum()
        fun forProductCode(productCode: Int): SensorType? = SensorType.values().firstOrNull { s -> s.productCode == productCode }
        fun forOrderNo(orderNo: String): SensorType? = SensorType.values().firstOrNull { s -> s.orderNo == orderNo }

        const val category = "HoTT_Sensor"
    }
}
