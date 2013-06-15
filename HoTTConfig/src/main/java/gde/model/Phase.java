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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public abstract class Phase {
	private Curve				channel1Curve;
	private Control[]		control;
	private DualRateExpo[] dualRateExpo;
	private boolean			motorOn;
	private String			number;
	private String			phaseName;
	private Switch			phaseSwitch;
	private double			phaseSwitchTime;
	private Clock				phaseTimer;
	private PhaseType		phaseType;
	public Curve getChannel1Curve() {
		return channel1Curve;
	}
	@XmlElementWrapper(name = "controls")
	public Control[] getControl() {
		return control;
	}

	@XmlElementWrapper(name = "dualRateExpos")
	public DualRateExpo[] getDualRateExpo() {
		return dualRateExpo;
	}

	@XmlAttribute
	@XmlID
	public String getNumber() {
		return number;
	}

	public String getPhaseName() {
		return phaseName;
	}

	@XmlIDREF
	public Switch getPhaseSwitch() {
		return phaseSwitch;
	}

	public double getPhaseSwitchTime() {
		return phaseSwitchTime;
	}

	@XmlIDREF
	public Clock getPhaseTimer() {
		return phaseTimer;
	}

	public PhaseType getPhaseType() {
		return phaseType;
	}

	public boolean isMotorOn() {
		return motorOn;
	}

	public void setChannel1Curve(final Curve channel1Curve) {
		this.channel1Curve = channel1Curve;
	}

	public void setControl(final Control[] controls) {
		control = controls;
	}

	public void setDualRateExpo(DualRateExpo[] dualRateExpo) {
		this.dualRateExpo = dualRateExpo;
	}

	public void setMotorOn(final boolean motorOn) {
		this.motorOn = motorOn;
	}

	public void setNumber(final int number) {
		this.number = Integer.toString(number);
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public void setPhaseName(final String phaseName) {
		this.phaseName = phaseName;
	}

	public void setPhaseSwitch(final Switch phaseSwitch) {
		this.phaseSwitch = phaseSwitch;
	}

	public void setPhaseSwitchTime(final double d) {
		phaseSwitchTime = d;
	}

	public void setPhaseTimer(final Clock phaseTimer) {
		this.phaseTimer = phaseTimer;
	}

	public void setPhaseType(final PhaseType phaseType) {
		this.phaseType = phaseType;
	}

	@Override
	public String toString() {
		return String.format("Phase %d: %s", Integer.parseInt(getNumber()) + 1, getPhaseName());
	}
}
