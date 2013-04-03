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

import gde.model.enums.PhaseType;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class Phase {
	private Control[] control;
	private DualRate[] dualRate;
	private Expo[] expo;
	private PhasedMixer mixer;
	private String name;
	private String number;
	private Switch sw;
	private PhasedTrim trim;
	private PhaseType type;

	@XmlElementWrapper(name = "controls")
	public Control[] getControl() {
		return control;
	}

	@XmlElementWrapper(name = "dualRates")
	public DualRate[] getDualRate() {
		return dualRate;
	}

	@XmlElementWrapper(name = "expos")
	public Expo[] getExpo() {
		return expo;
	}

	@XmlElement(name = "phasemixer")
	public PhasedMixer getMixer() {
		return mixer;
	}

	public String getName() {
		return name;
	}

	@XmlAttribute
	@XmlID
	public String getNumber() {
		return number;
	}

	@XmlIDREF
	public Switch getSwitch() {
		return sw;
	}

	@XmlElement(name = "phasetrim")
	public PhasedTrim getTrim() {
		return trim;
	}

	public PhaseType getType() {
		return type;
	}

	public void setControl(final Control[] controls) {
		control = controls;
	}

	public void setDualRate(final DualRate[] dualRate) {
		this.dualRate = dualRate;
	}

	public void setExpo(final Expo[] expo) {
		this.expo = expo;
	}

	public void setMixer(final PhasedMixer mixer) {
		this.mixer = mixer;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setNumber(final int number) {
		this.number = Integer.toString(number);
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}

	public void setTrim(final PhasedTrim trim) {
		this.trim = trim;
	}

	public void setType(final PhaseType type) {
		this.type = type;
	}
}
