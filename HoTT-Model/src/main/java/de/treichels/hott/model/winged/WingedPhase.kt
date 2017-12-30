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

import de.treichels.hott.model.Curve
import de.treichels.hott.model.Phase
import javax.xml.bind.annotation.XmlElementWrapper

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
class WingedPhase(
        var brakeElevatorCurve: Curve = Curve(),
        @get:XmlElementWrapper(name = "brakeMixers")
        var brakeMixer: List<WingedMixer> = listOf(),
        @get:XmlElementWrapper(name = "multiFlapMixers")
        var multiFlapMixer: List<WingedMixer> = mutableListOf(),
        @get:XmlElementWrapper(name = "wingMixers")
        var wingMixer: List<WingedMixer> = mutableListOf(),
        var wingTrim: WingedTrim = WingedTrim()
) : Phase() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WingedPhase) return false
        if (!super.equals(other)) return false

        if (brakeElevatorCurve != other.brakeElevatorCurve) return false
        if (brakeMixer != other.brakeMixer) return false
        if (multiFlapMixer != other.multiFlapMixer) return false
        if (wingMixer != other.wingMixer) return false
        if (wingTrim != other.wingTrim) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + brakeElevatorCurve.hashCode()
        result = 31 * result + brakeMixer.hashCode()
        result = 31 * result + multiFlapMixer.hashCode()
        result = 31 * result + wingMixer.hashCode()
        result = 31 * result + wingTrim.hashCode()
        return result
    }
}
