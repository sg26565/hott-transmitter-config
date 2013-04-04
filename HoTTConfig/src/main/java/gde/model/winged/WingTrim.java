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

import gde.model.PhasedTrim;
import gde.model.enums.PhaseType;

/**
 * @author oli@treichels.de
 */
public class WingTrim extends PhasedTrim {
	private int aileron2PhaseTrim;
	private int aileronPhaseTrim;
	private int aileronStickTrim;
	private int elevatorPhaseTrim;
	private int elevtorStickTrim;
	private int flap2PhaseTrim;
	private int flapPhaseTrim;
	private PhaseType phaseType;
	private int rudderStickTrim;

	public int getAileron2PhaseTrim() {
		return aileron2PhaseTrim;
	}

	public int getAileronPhaseTrim() {
		return aileronPhaseTrim;
	}

	public int getAileronStickTrim() {
		return aileronStickTrim;
	}

	public int getElevatorPhaseTrim() {
		return elevatorPhaseTrim;
	}

	public int getElevtorStickTrim() {
		return elevtorStickTrim;
	}

	public int getFlap2PhaseTrim() {
		return flap2PhaseTrim;
	}

	public int getFlapPhaseTrim() {
		return flapPhaseTrim;
	}

	public PhaseType getPhaseType() {
		return phaseType;
	}

	public int getRudderStickTrim() {
		return rudderStickTrim;
	}

	public void setAileron2PhaseTrim(final int aileron2PhaseTrim) {
		this.aileron2PhaseTrim = aileron2PhaseTrim;
	}

	public void setAileronPhaseTrim(final int aileronPhaseTrim) {
		this.aileronPhaseTrim = aileronPhaseTrim;
	}

	public void setAileronStickTrim(final int aileronStickTrim) {
		this.aileronStickTrim = aileronStickTrim;
	}

	public void setElevatorPhaseTrim(final int elevatorPhaseTrim) {
		this.elevatorPhaseTrim = elevatorPhaseTrim;
	}

	public void setElevtorStickTrim(final int elevtorStickTrim) {
		this.elevtorStickTrim = elevtorStickTrim;
	}

	public void setFlap2PhaseTrim(final int flap2PhaseTrim) {
		this.flap2PhaseTrim = flap2PhaseTrim;
	}

	public void setFlapPhaseTrim(final int flapPhaseTrim) {
		this.flapPhaseTrim = flapPhaseTrim;
	}

	public void setPhaseType(final PhaseType phaseType) {
		this.phaseType = phaseType;
	}

	public void setRudderStickTrim(final int rudderStickTrim) {
		this.rudderStickTrim = rudderStickTrim;
	}
}
