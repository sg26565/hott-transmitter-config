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

import javax.xml.bind.annotation.XmlElementWrapper;

import gde.model.Curve;
import gde.model.Phase;

/**
 * @author oli
 *
 */
public class WingedPhase extends Phase {
	private static final long serialVersionUID = 1L;

	private Curve brakeElevatorCurve;
	private WingedMixer[] brakeMixer;
	private WingedMixer[] multiFlapMixer;
	private WingedMixer[] wingMixer;
	private WingedTrim wingTrim;

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final WingedPhase other = (WingedPhase) obj;
		if (brakeElevatorCurve == null) {
			if (other.brakeElevatorCurve != null) {
				return false;
			}
		} else if (!brakeElevatorCurve.equals(other.brakeElevatorCurve)) {
			return false;
		}
		if (!Arrays.equals(brakeMixer, other.brakeMixer)) {
			return false;
		}
		if (!Arrays.equals(multiFlapMixer, other.multiFlapMixer)) {
			return false;
		}
		if (!Arrays.equals(wingMixer, other.wingMixer)) {
			return false;
		}
		if (wingTrim == null) {
			if (other.wingTrim != null) {
				return false;
			}
		} else if (!wingTrim.equals(other.wingTrim)) {
			return false;
		}
		return true;
	}

	public Curve getBrakeElevatorCurve() {
		return brakeElevatorCurve;
	}

	@XmlElementWrapper(name = "brakeMixers")
	public WingedMixer[] getBrakeMixer() {
		return brakeMixer;
	}

	@XmlElementWrapper(name = "multiFlapMixers")
	public WingedMixer[] getMultiFlapMixer() {
		return multiFlapMixer;
	}

	@XmlElementWrapper(name = "wingMixers")
	public WingedMixer[] getWingMixer() {
		return wingMixer;
	}

	public WingedTrim getWingTrim() {
		return wingTrim;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (brakeElevatorCurve == null ? 0 : brakeElevatorCurve.hashCode());
		result = prime * result + Arrays.hashCode(brakeMixer);
		result = prime * result + Arrays.hashCode(multiFlapMixer);
		result = prime * result + Arrays.hashCode(wingMixer);
		result = prime * result + (wingTrim == null ? 0 : wingTrim.hashCode());
		return result;
	}

	public void setBrakeElevatorCurve(final Curve brakeElevatorCurve) {
		this.brakeElevatorCurve = brakeElevatorCurve;
	}

	public void setBrakeMixer(final WingedMixer[] brakeMixer) {
		this.brakeMixer = brakeMixer;
	}

	public void setMultiFlapMixer(final WingedMixer[] multiFlapMixer) {
		this.multiFlapMixer = multiFlapMixer;
	}

	public void setWingMixer(final WingedMixer[] wingMixer) {
		this.wingMixer = wingMixer;
	}

	public void setWingTrim(final WingedTrim wingTrim) {
		this.wingTrim = wingTrim;
	}
}
