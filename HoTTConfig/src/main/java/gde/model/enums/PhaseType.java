/**
 *  HoTT Transmitter Config
 *  Copyright (C) 2013  Oliver Treichel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package gde.model.enums;

import java.util.ResourceBundle;

/**
 * @author oli@treichels.de
 */
public enum PhaseType {
	Acro, Acro_3D, Acro2, AirTow, Autorotation, Custom1, Custom10, Custom2, Custom3, Custom4, Custom5, Custom6, Custom7, Custom8, Custom9, Distance, Distance2, Global, Hover, Landing, landing2, Normal, Speed, Speed2, Takeoff, Takeoff2, Test, Test2, Thermal, Thermal2, Unnamed, Unused;

	/** @return the locale-dependent message */
	@Override
	public String toString() {
		return ResourceBundle.getBundle(getClass().getName()).getString(name());
	}
}
