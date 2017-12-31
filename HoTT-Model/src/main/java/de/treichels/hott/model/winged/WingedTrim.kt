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
package de.treichels.hott.model.winged

import de.treichels.hott.model.AbstractBase

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
data class WingedTrim(
        var aileronPhaseTrim: List<Int> = emptyList(),
        var aileronStickTrim: Int = 0,
        var elevatorPhaseTrim: Int = 0,
        var elevatorStickTrim: Int = 0,
        var flapPhaseTrim: List<Int> = emptyList(),
        var rudderStickTrim: Int = 0
) : AbstractBase()