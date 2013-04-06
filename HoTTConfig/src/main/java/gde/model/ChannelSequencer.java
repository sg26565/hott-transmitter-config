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

import gde.model.enums.SequenceStatus;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli
 */
public class ChannelSequencer {
	private int maxStep;
	private SequenceStatus powerOffStatus;
	private Sequence[] sequence;
	private int[] stepTime;
	private Switch sw;

	public int getMaxStep() {
		return maxStep;
	}

	public SequenceStatus getPowerOffStatus() {
		return powerOffStatus;
	}

	@XmlElementWrapper(name = "sequences")
	public Sequence[] getSequence() {
		return sequence;
	}

	@XmlElementWrapper(name = "stepTimes")
	public int[] getStepTime() {
		return stepTime;
	}

	@XmlIDREF
	public Switch getSwitch() {
		return sw;
	}

	public void setMaxStep(final int maxStep) {
		this.maxStep = maxStep;
	}

	public void setPowerOffStatus(final SequenceStatus powerOffStatus) {
		this.powerOffStatus = powerOffStatus;
	}

	public void setSequence(final Sequence[] sequence) {
		this.sequence = sequence;
	}

	public void setStepTime(final int[] stepTime) {
		this.stepTime = stepTime;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}
}
