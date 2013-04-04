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

import gde.model.enums.ControlMode;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class Control {
	private Switch controlSwitch;
	private Switch highSwitch;
	private ControlMode mode;
	private String number;
	private int offset;
	private Switch lowSwitch;
	private int timeHigh;

	private int timeLow;

	private int travelHigh;

	private int travelLow;

	@XmlIDREF
	public Switch getControlSwitch() {
		return controlSwitch;
	}

	@XmlIDREF
	public Switch getHighSwitch() {
		return highSwitch;
	}

	public ControlMode getMode() {
		return mode;
	}

	@XmlAttribute
	@XmlID
	public String getNumber() {
		return number;
	}

	public int getOffset() {
		return offset;
	}

	@XmlIDREF
	public Switch getLowSwitch() {
		return lowSwitch;
	}

	public int getTimeHigh() {
		return timeHigh;
	}

	public int getTimeLow() {
		return timeLow;
	}

	public int getTravelHigh() {
		return travelHigh;
	}

	public int getTravelLow() {
		return travelLow;
	}

	public void setControlSwitch(final Switch controlSwitch) {
		this.controlSwitch = controlSwitch;
	}

	public void setHighSwitch(final Switch leftSwitch) {
		this.highSwitch = leftSwitch;
	}

	public void setMode(final ControlMode mode) {
		this.mode = mode;
	}

	public void setNumber(final int number) {
		this.number = Integer.toString(number);
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public void setOffset(final int offset) {
		this.offset = offset;
	}

	public void setLowSwitch(final Switch rightSwitch) {
		this.lowSwitch = rightSwitch;
	}

	public void setTimeHigh(final int timeHigh) {
		this.timeHigh = timeHigh;
	}

	public void setTimeLow(final int timeLow) {
		this.timeLow = timeLow;
	}

	public void setTravelHigh(final int travelHigh) {
		this.travelHigh = travelHigh;
	}

	public void setTravelLow(final int travelLow) {
		this.travelLow = travelLow;
	}
}
