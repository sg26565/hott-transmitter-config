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

import tornadofx.*
import java.util.*
import java.util.stream.Stream

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
enum class ReceiverType(val id: Int) {
    E06(1), E08(0), E09(3), E12(5), E16(7);

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]

    companion object {
        fun forId(id: Int): ReceiverType? {
            return Stream.of(*values()).filter { s -> s.id == id }.findFirst().orElse(null)
        }
    }
}
