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

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
enum class SensorType(val code: Int) {
    ElectricAirModule(1 shl 2), ESC(1 shl 5), GeneralModule(1 shl 1), GPS(1 shl 4), None(0), Receiver(1 shl 0), Vario(1 shl 3);

    companion object {
        fun forCode(code: Int): List<SensorType> {
            return SensorType.values().filter { s -> s.code and code != 0 }.toList()
        }

        fun getCode(sensors: List<SensorType>): Int {
            return sensors.stream().mapToInt{ it.code }.sum()
        }
    }
}
