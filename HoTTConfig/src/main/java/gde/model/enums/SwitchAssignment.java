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

public enum SwitchAssignment {
	DG1, DG2, DG3, DG4, DG5, FX, FXi, G1, G2, G3, G4, G5, G6, G7, G8, GB1, GB10, GB2, GB3, GB4, GB5, GB6, GB7, GB8, GB9, Gi1, Gi2, Gi3, Gi4, Gi5, Gi6, Gi7, Gi8, L1, L2, L3, L4, L5, L6, L7, L8, Li1, Li2, Li3, Li4, Li5, Li6, Li7, Li8, SD1, SD2, SR1, SR2, SR3, SW1, SW10, SW11, SW12, SW13, SW14, SW15, SW16, SW2, SW3, SW4, SW5, SW6, SW7, SW8, SW9, TR1, TR2, TR3, TR4, UG1, UG2, Unassigned, Unknown;

	/** @return the locale-dependent message */
	@Override
	public String toString() {
		return ResourceBundle.getBundle(getClass().getName()).getString(name());
	}
}
