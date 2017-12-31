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

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
enum class AileronFlapType(val ailerons: Int, val flaps: Int) {
    FourAilFourFlap(4, 4), FourAilTwoFlap(4, 2), OneAil(1, 0), OneAilOneFlap(1, 1), TwoAil(2, 0), TwoAilFourFlap(2, 4), TwoAilOneFlap(2, 1),
    TwoAilTwoFlap(2, 2);

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]
}