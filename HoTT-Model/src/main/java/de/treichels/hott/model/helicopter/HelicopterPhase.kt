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
package de.treichels.hott.model.helicopter

import de.treichels.hott.model.Phase

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
class HelicopterPhase(
        var channel8Value: Int = 0,
        var gyroGain: Int = 0,
        var gyroSuppression: Int = 0,
        var helicopterMixer: HelicopterMixer = HelicopterMixer(),
        var helicopterTrim: HelicopterTrim = HelicopterTrim()
) : Phase() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HelicopterPhase) return false
        if (!super.equals(other)) return false

        if (channel8Value != other.channel8Value) return false
        if (gyroGain != other.gyroGain) return false
        if (gyroSuppression != other.gyroSuppression) return false
        if (helicopterMixer != other.helicopterMixer) return false
        if (helicopterTrim != other.helicopterTrim) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + channel8Value
        result = 31 * result + gyroGain
        result = 31 * result + gyroSuppression
        result = 31 * result + helicopterMixer.hashCode()
        result = 31 * result + helicopterTrim.hashCode()
        return result
    }
}
