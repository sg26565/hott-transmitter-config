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
import gde.model.enums.SwitchFunction;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;

public class WingedMixer {
	private SwitchFunction function;
	private Switch sw;
	private int value;

	public WingedMixer(final SwitchFunction function) {
		this.function = function;
	}

	@XmlAttribute
	public SwitchFunction getFunction() {
		return function;
	}

	@XmlIDREF
	public Switch getSwitch() {
		return sw;
	}

	public int getValue() {
		return value;
	}

	public void setFunction(final SwitchFunction function) {
		this.function = function;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}

	public void setValue(final int value) {
		this.value = value;
	}
}
