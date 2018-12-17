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
enum class SensorType {
    Receiver, GeneralModule, ElectricAirModule, Vario, GPS, ESC, None;

    val map = 1 shl ordinal and 0b111111

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]

    companion object {
        fun forMap(code: Int): List<SensorType> {
            return SensorType.values().filter { s -> s.map and code != 0 }.toList()
        }

        fun getMap(sensors: List<SensorType>): Int {
            return sensors.stream().mapToInt { it.map }.sum()
        }
    }
}
