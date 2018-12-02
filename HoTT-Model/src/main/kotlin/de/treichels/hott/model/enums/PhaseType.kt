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
    Acro, Acro_3D, Acro2, AirTow, Autorotation, Custom1, Custom10, Custom2, Custom3, Custom4, Custom5, Custom6, Custom7, Custom8, Custom9, Distance, Distance2,
    Global, Hover, Landing, Landing2, Normal, Speed, Speed2, Takeoff, Takeoff2, Test, Test2, Thermal, Thermal2, Unnamed, Unused;

override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]
}
