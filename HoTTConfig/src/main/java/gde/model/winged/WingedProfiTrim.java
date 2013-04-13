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

import gde.model.Switch;

import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli
 */
public class WingedProfiTrim {
	private boolean	enabled;
	private Switch	inputControl;

	@XmlIDREF
	public Switch getInputControl() {
		return inputControl;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public void setInputControl(final Switch inputControl) {
		this.inputControl = inputControl;
	}
}
