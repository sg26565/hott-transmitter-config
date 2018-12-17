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
enum class StickMode {
    Undefined, Mode1, Mode2, Mode3, Mode4, Unknown;

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]
}
