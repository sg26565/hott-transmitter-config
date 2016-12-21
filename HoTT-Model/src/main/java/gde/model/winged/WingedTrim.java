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
package gde.model.winged;

import java.util.Arrays;

import gde.model.AbstractBase;

/**
 * @author oli@treichels.de
 */
public class WingedTrim extends AbstractBase {
	private static final long serialVersionUID = 1L;

	private int[] aileronPhaseTrim;
	private int aileronStickTrim;
	private int elevatorPhaseTrim;
	private int elevatorStickTrim;
	private int[] flapPhaseTrim;
	private int rudderStickTrim;

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final WingedTrim other = (WingedTrim) obj;
		if (!Arrays.equals(aileronPhaseTrim, other.aileronPhaseTrim)) {
			return false;
		}
		if (aileronStickTrim != other.aileronStickTrim) {
			return false;
		}
		if (elevatorPhaseTrim != other.elevatorPhaseTrim) {
			return false;
		}
		if (elevatorStickTrim != other.elevatorStickTrim) {
			return false;
		}
		if (!Arrays.equals(flapPhaseTrim, other.flapPhaseTrim)) {
			return false;
		}
		if (rudderStickTrim != other.rudderStickTrim) {
			return false;
		}
		return true;
	}

	public int[] getAileronPhaseTrim() {
		return aileronPhaseTrim;
	}

	public int getAileronStickTrim() {
		return aileronStickTrim;
	}

	public int getElevatorPhaseTrim() {
		return elevatorPhaseTrim;
	}

	public int getElevatorStickTrim() {
		return elevatorStickTrim;
	}

	public int[] getFlapPhaseTrim() {
		return flapPhaseTrim;
	}

	public int getRudderStickTrim() {
		return rudderStickTrim;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(aileronPhaseTrim);
		result = prime * result + aileronStickTrim;
		result = prime * result + elevatorPhaseTrim;
		result = prime * result + elevatorStickTrim;
		result = prime * result + Arrays.hashCode(flapPhaseTrim);
		result = prime * result + rudderStickTrim;
		return result;
	}

	public void setAileronPhaseTrim(final int[] aileronPhaseTrim) {
		this.aileronPhaseTrim = aileronPhaseTrim;
	}

	public void setAileronStickTrim(final int aileronStickTrim) {
		this.aileronStickTrim = aileronStickTrim;
	}

	public void setElevatorPhaseTrim(final int elevatorPhaseTrim) {
		this.elevatorPhaseTrim = elevatorPhaseTrim;
	}

	public void setElevatorStickTrim(final int elevtorStickTrim) {
		elevatorStickTrim = elevtorStickTrim;
	}

	public void setFlapPhaseTrim(final int[] flapPhaseTrim) {
		this.flapPhaseTrim = flapPhaseTrim;
	}

	public void setRudderStickTrim(final int rudderStickTrim) {
		this.rudderStickTrim = rudderStickTrim;
	}
}
