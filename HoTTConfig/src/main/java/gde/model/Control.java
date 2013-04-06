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
	private Switch inputControl;
	private ControlMode mode;
	private String number;
	private int offset;
	private double timeHigh;
	private double timeLow;
	private Switch toggleHighSwitch;
	private Switch toggleLowSwitch;
	private int travelHigh;
	private int travelLow;
	private int trim;

	@XmlIDREF
	public Switch getInputControl() {
		return inputControl;
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

	public double getTimeHigh() {
		return timeHigh;
	}

	public double getTimeLow() {
		return timeLow;
	}

	@XmlIDREF
	public Switch getToggleHighSwitch() {
		return toggleHighSwitch;
	}

	@XmlIDREF
	public Switch getToggleLowSwitch() {
		return toggleLowSwitch;
	}

	public int getTravelHigh() {
		return travelHigh;
	}

	public int getTravelLow() {
		return travelLow;
	}

	public int getTrim() {
		return trim;
	}

	public void setInputControl(final Switch controlSwitch) {
		inputControl = controlSwitch;
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

	public void setTimeHigh(final double timeHigh) {
		this.timeHigh = timeHigh;
	}

	public void setTimeLow(final double timeLow) {
		this.timeLow = timeLow;
	}

	public void setToggleHighSwitch(final Switch leftSwitch) {
		toggleHighSwitch = leftSwitch;
	}

	public void setToggleLowSwitch(final Switch rightSwitch) {
		toggleLowSwitch = rightSwitch;
	}

	public void setTravelHigh(final int travelHigh) {
		this.travelHigh = travelHigh;
	}

	public void setTravelLow(final int travelLow) {
		this.travelLow = travelLow;
	}

	public void setTrim(final int trim) {
		this.trim = trim;
	}
}
