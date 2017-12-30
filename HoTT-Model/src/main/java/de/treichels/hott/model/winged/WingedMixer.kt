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
import de.treichels.hott.model.Switch
import de.treichels.hott.model.enums.SwitchFunction
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlID
import javax.xml.bind.annotation.XmlIDREF

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
