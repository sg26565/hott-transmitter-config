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

import de.treichels.hott.model.BaseModel
import de.treichels.hott.model.Switch
import de.treichels.hott.model.enums.ModelType
import de.treichels.hott.model.enums.PitchMin
import de.treichels.hott.model.enums.RotorDirection
import de.treichels.hott.model.enums.SwashplateType
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlIDREF
import javax.xml.bind.annotation.XmlRootElement

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
@XmlRootElement
open class HelicopterModel(
        modelType: ModelType = ModelType.Helicopter,
        var autorotationC1TriggerPosition: Int = 0,
        @get:XmlIDREF
        var autorotationSwitch: Switch = Switch(),
        var autorotationTailPosition: Int = 0,
        var autorotationThrottlePosition: Int = 0,
        var expoThrottleLimit: Int = 0,
        @get:XmlIDREF
        var markerSwitch: Switch = Switch(),
        var pitchMin: PitchMin? = null,
        @get:XmlElementWrapper(name = "profitrims")
        var profiTrim: List<HelicopterProfiTrim> = emptyList(),
        var rotorDirection: RotorDirection? = null,
        var isSwashplateLinearization: Boolean = false,
        var swashplateMix: SwashplateMix? = null,
        var swashplateType: SwashplateType? = null,
        var throttleLimitWarning: Int = 0,
        var isThrottleMarkerActive: Boolean = false,
        var throttleMarkerPosition: Int = 0
) : BaseModel(modelType) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HelicopterModel) return false
        if (!super.equals(other)) return false

        if (autorotationC1TriggerPosition != other.autorotationC1TriggerPosition) return false
        if (autorotationSwitch != other.autorotationSwitch) return false
        if (autorotationTailPosition != other.autorotationTailPosition) return false
        if (autorotationThrottlePosition != other.autorotationThrottlePosition) return false
        if (expoThrottleLimit != other.expoThrottleLimit) return false
        if (markerSwitch != other.markerSwitch) return false
        if (pitchMin != other.pitchMin) return false
        if (profiTrim != other.profiTrim) return false
        if (rotorDirection != other.rotorDirection) return false
        if (isSwashplateLinearization != other.isSwashplateLinearization) return false
        if (swashplateMix != other.swashplateMix) return false
        if (swashplateType != other.swashplateType) return false
        if (throttleLimitWarning != other.throttleLimitWarning) return false
        if (isThrottleMarkerActive != other.isThrottleMarkerActive) return false
        if (throttleMarkerPosition != other.throttleMarkerPosition) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + autorotationC1TriggerPosition
        result = 31 * result + autorotationSwitch.hashCode()
        result = 31 * result + autorotationTailPosition
        result = 31 * result + autorotationThrottlePosition
        result = 31 * result + expoThrottleLimit
        result = 31 * result + markerSwitch.hashCode()
        result = 31 * result + (pitchMin?.hashCode() ?: 0)
        result = 31 * result + profiTrim.hashCode()
        result = 31 * result + (rotorDirection?.hashCode() ?: 0)
        result = 31 * result + isSwashplateLinearization.hashCode()
        result = 31 * result + (swashplateMix?.hashCode() ?: 0)
        result = 31 * result + (swashplateType?.hashCode() ?: 0)
        result = 31 * result + throttleLimitWarning
        result = 31 * result + isThrottleMarkerActive.hashCode()
        result = 31 * result + throttleMarkerPosition
        return result
    }
}
