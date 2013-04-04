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

import gde.model.enums.MixerInputType;
import gde.model.enums.MixerType;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class FreeMixer {
	private Channel fromChannel;
	private MixerInputType inputType;
	private String number;
	private FreeMixerPhaseSetting[] phaseSetting;
	private Switch sw;
	private Channel toChannel;
	private MixerType type;

	@XmlIDREF
	public Channel getFromChannel() {
		return fromChannel;
	}

	public MixerInputType getInputType() {
		return inputType;
	}

	@XmlAttribute
	@XmlID
	public String getNumber() {
		return number;
	}

	@XmlElementWrapper(name = "phaseSettings")
	public FreeMixerPhaseSetting[] getPhaseSetting() {
		return phaseSetting;
	}

	@XmlIDREF
	public Switch getSwitch() {
		return sw;
	}

	@XmlIDREF
	public Channel getToChannel() {
		return toChannel;
	}

	public MixerType getType() {
		return type;
	}

	public void setFromChannel(final Channel from) {
		fromChannel = from;
	}

	public void setInputType(final MixerInputType inputType) {
		this.inputType = inputType;
	}

	public void setNumber(final int number) {
		this.number = Integer.toString(number);
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public void setPhaseSetting(final FreeMixerPhaseSetting[] phaseSetting) {
		this.phaseSetting = phaseSetting;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}

	public void setToChannel(final Channel to) {
		toChannel = to;
	}

	public void setType(final MixerType type) {
		this.type = type;
	}
}
