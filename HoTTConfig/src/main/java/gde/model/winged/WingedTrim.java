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

/**
 * @author oli@treichels.de
 */
public class WingedTrim {
	private int aileron1PhaseTrim;
	private int aileron2PhaseTrim;
	private int aileronStickTrim;
	private int elevatorPhaseTrim;
	private int elevtorStickTrim;
	private int flap1PhaseTrim;
	private int flap2PhaseTrim;
	private int rudderStickTrim;

	public int getAileron1PhaseTrim() {
		return aileron1PhaseTrim;
	}

	public int getAileron2PhaseTrim() {
		return aileron2PhaseTrim;
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

	public int getFlap1PhaseTrim() {
		return flap1PhaseTrim;
	}

	public int getFlap2PhaseTrim() {
		return flap2PhaseTrim;
	}

	public int getRudderStickTrim() {
		return rudderStickTrim;
	}

	public void setAileron1PhaseTrim(final int aileronPhaseTrim) {
		aileron1PhaseTrim = aileronPhaseTrim;
	}

	public void setAileron2PhaseTrim(final int aileron2PhaseTrim) {
		this.aileron2PhaseTrim = aileron2PhaseTrim;
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

	public void setFlap1PhaseTrim(final int flapPhaseTrim) {
		flap1PhaseTrim = flapPhaseTrim;
	}

	public void setFlap2PhaseTrim(final int flap2PhaseTrim) {
		this.flap2PhaseTrim = flap2PhaseTrim;
	}

	public void setRudderStickTrim(final int rudderStickTrim) {
		this.rudderStickTrim = rudderStickTrim;
	}
}
