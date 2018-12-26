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
enum class PhaseType {
    Custom10, Custom9, Custom8, Custom7, Custom6, Custom5, Custom4, Custom3, Custom2, Custom1, Unused,
    Normal, Takeoff, Takeoff2, Thermal, Thermal2, Distance, Distance2, Speed, Speed2,
    Acro, Acro2, Landing, Landing2, AirTow, Test, Test2,
    Acro_3D, Autorotation, Global, Hover, Hover2, Unnamed,
    Phase1, Phase2, Phase3, Phase4, Phase5, Phase6, Phase7, Phase8;

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]
}
