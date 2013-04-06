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
package gde.model.helicopter;

import gde.model.Phase;
import gde.model.Switch;
import gde.model.enums.CurveType;

import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli
 */
public class HelicopterProfiTrim {
	private CurveType curveType;
	private boolean enabled;
	private Switch inputControl;
	private Phase phase;
	private int point;

	public CurveType getCurveType() {
		return curveType;
	}

	@XmlIDREF
	public Switch getInputControl() {
		return inputControl;
	}

	@XmlIDREF
	public Phase getPhase() {
		return phase;
	}

	public int getPoint() {
		return point;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setCurveType(final CurveType curveType) {
		this.curveType = curveType;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public void setInputControl(final Switch inputControl) {
		this.inputControl = inputControl;
	}

	public void setPhase(final Phase phase) {
		this.phase = phase;
	}

	public void setPoint(final int point) {
		this.point = point;
	}
}
