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

import de.treichels.hott.model.BaseModel
import de.treichels.hott.model.Channel
import de.treichels.hott.model.Switch
import de.treichels.hott.model.enums.AileronFlapType
import de.treichels.hott.model.enums.ModelType
import de.treichels.hott.model.enums.MotorOnC1Type
import de.treichels.hott.model.enums.TailType
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlIDREF
import javax.xml.bind.annotation.XmlRootElement

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
@XmlRootElement
class WingedModel : BaseModel(ModelType.Winged) {
    var aileronFlapType: AileronFlapType? = null
    @get:XmlIDREF
    var brakeInputChannel: Channel? = null
    var brakeOffset: Int = 0
    var isChannel8Delay: Boolean = false
    var motorOnC1Type: MotorOnC1Type? = null
    @get:XmlElementWrapper(name = "profitrims")
    var profiTrim: List<WingedProfiTrim> = emptyList()
    @get:XmlIDREF
    var profiTrimSwitch: Switch? = null
    var tailType: TailType? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WingedModel) return false
        if (!super.equals(other)) return false

        if (aileronFlapType != other.aileronFlapType) return false
        if (brakeInputChannel != other.brakeInputChannel) return false
        if (brakeOffset != other.brakeOffset) return false
        if (isChannel8Delay != other.isChannel8Delay) return false
        if (motorOnC1Type != other.motorOnC1Type) return false
        if (profiTrim != other.profiTrim) return false
        if (profiTrimSwitch != other.profiTrimSwitch) return false
        if (tailType != other.tailType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (aileronFlapType?.hashCode() ?: 0)
        result = 31 * result + (brakeInputChannel?.hashCode() ?: 0)
        result = 31 * result + brakeOffset
        result = 31 * result + isChannel8Delay.hashCode()
        result = 31 * result + (motorOnC1Type?.hashCode() ?: 0)
        result = 31 * result + profiTrim.hashCode()
        result = 31 * result + (profiTrimSwitch?.hashCode() ?: 0)
        result = 31 * result + (tailType?.hashCode() ?: 0)
        return result
    }
}
