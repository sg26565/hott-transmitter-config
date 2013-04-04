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
package gde.model;

import gde.model.enums.ClockMode;
import gde.model.enums.ClockType;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class Clock {
	private ClockMode mode;
	private String number;
	private Switch sw;
	private ClockType type;

	private int value;

	public int getMinutes() {
		return getValue() / 60;
	}

	public ClockMode getMode() {
		return mode;
	}

	@XmlAttribute
	@XmlID
	public String getNumber() {
		return number;
	}

	public int getSeconds() {
		return getValue() % 60;
	}

	@XmlIDREF
	public Switch getSwitch() {
		return sw;
	}

	public ClockType getType() {
		return type;
	}

	public int getValue() {
		return value;
	}

	public void setMinutes(final int minutes) {
		setValue(minutes * 60 + getSeconds());
	}

	public void setMode(final ClockMode mode) {
		this.mode = mode;
	}

	public void setNumber(final int number) {
		this.number = Integer.toString(number);
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public void setSeconds(final int seconds) {
		setValue(getMinutes() * 60 + seconds);
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}

	public void setType(final ClockType type) {
		this.type = type;
	}

	public void setValue(final int value) {
		this.value = value;
	}
}
