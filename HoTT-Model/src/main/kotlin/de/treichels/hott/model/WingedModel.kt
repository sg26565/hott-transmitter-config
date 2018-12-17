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
package de.treichels.hott.model

import de.treichels.hott.model.enums.*
import javax.xml.bind.annotation.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
class WingedModel : BaseModel(ModelType.Winged) {
    var aileronFlapType: AileronFlapType = AileronFlapType.Unknown
    @get:XmlIDREF
    var brakeInputChannel: Channel = Channel()
    var brakeOffset: Int = 0
    var isChannel8Delay: Boolean = false
    @get:XmlElementWrapper(name = "profitrims")
    var profiTrim: List<WingedProfiTrim> = emptyList()
    @get:XmlIDREF
    var profiTrimSwitch: Switch = Switch()
    var tailType: TailType = TailType.Unknown
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as WingedModel

        if (aileronFlapType != other.aileronFlapType) return false
        if (brakeOffset != other.brakeOffset) return false
        if (isChannel8Delay != other.isChannel8Delay) return false
        if (tailType != other.tailType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + aileronFlapType.hashCode()
        result = 31 * result + brakeOffset
        result = 31 * result + isChannel8Delay.hashCode()
        result = 31 * result + tailType.hashCode()
        return result
    }

}

data class WingedMixer(
        var function: SwitchFunction = SwitchFunction.Unassigned,
        var qualifier: List<Any> = emptyList(),
        @get:XmlIDREF
        var switch: Switch = Switch(),
        var value: List<Int> = emptyList()
) : AbstractBase() {
    val id: String
        @XmlID
        @XmlAttribute
        get() {
            val b = StringBuilder()

            b.append(function.name)

            for (q in qualifier) {
                b.append("_")

                if (q.javaClass.isEnum)
                    try {
                        val m = q.javaClass.getMethod("name")
                        b.append(m.invoke(q))
                    } catch (e: Exception) {
                        throw RuntimeException(e)
                    }
                else
                    b.append(q.toString())
            }

            return b.toString()
        }

    fun setValue(value: Int) {
        this.value = listOf(value)
    }
}

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

data class WingedProfiTrim(
        var isEnabled: Boolean = false,
        @get:XmlIDREF
        var inputControl: Switch = Switch()
) : AbstractBase()

data class WingedTrim(
        var aileronPhaseTrim: List<Int> = emptyList(),
        var aileronStickTrim: Int = 0,
        var elevatorPhaseTrim: Int = 0,
        var elevatorStickTrim: Int = 0,
        var flapPhaseTrim: List<Int> = emptyList(),
        var rudderStickTrim: Int = 0
) : AbstractBase()
