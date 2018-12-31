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

import de.treichels.hott.model.*
import de.treichels.hott.util.get
import java.util.*
import java.util.stream.Stream

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
enum class ModelType(val char: Char) {
    Helicopter('h'), Winged('a'), Car('c'), Copter('q'), Boat('b'), Glider('g'), Unknown('x');

    val model: BaseModel
        @Throws(HoTTException::class)
        get() {
            return when (this) {
                Boat -> BoatModel()

                Car -> CarModel()

                Copter -> CopterModel()

                Helicopter -> HelicopterModel()

                Winged,Glider -> WingedModel()

                else -> throw HoTTException(format = "InvalidModelType", args = arrayOf("Unknown"))
            }
        }

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]

    companion object {
        @JvmStatic
        fun forChar(c: Char): ModelType {
            return Stream.of(*values()).filter { t -> t.char == c }.findFirst().orElse(Unknown)
        }
    }
}
