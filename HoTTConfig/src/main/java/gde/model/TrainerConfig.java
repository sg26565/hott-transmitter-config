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
 *  along with this program.  If not, see <http://www.gnu.org/licenses/[].
 */
package gde.model;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class TrainerConfig {
	private Collection<Channel> pupilChannels;
	private long pupilId;
	private Switch sw;
	private Collection<Channel> trainerChannels;
	private long trainerId;
	private boolean wireless;

	public TrainerConfig() {
	}

	@XmlIDREF
	@XmlElement(name = "pupilChannel")
	public Collection<Channel> getPupilChannels() {
		return pupilChannels;
	}

	public long getPupilId() {
		return pupilId;
	}

	@XmlIDREF
	@XmlElement(name = "trainerSwitch")
	public Switch getSwitch() {
		return sw;
	}

	@XmlIDREF
	@XmlElement(name = "trainerChannel")
	public Collection<Channel> getTrainerChannels() {
		return trainerChannels;
	}

	public long getTrainerId() {
		return trainerId;
	}

	public boolean isWireless() {
		return wireless;
	}

	public void setPupilChannels(final Collection<Channel> pupilChannels) {
		this.pupilChannels = pupilChannels;
	}

	public void setPupilId(final long pupilId) {
		this.pupilId = pupilId;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}

	public void setTrainerChannels(final Collection<Channel> trainerChannels) {
		this.trainerChannels = trainerChannels;
	}

	public void setTrainerId(final long trainerId) {
		this.trainerId = trainerId;
	}

	public void setWireless(final boolean wireless) {
		this.wireless = wireless;
	}
}