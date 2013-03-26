package gde.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class TrainerConfig {
	private List<Channel> pupilChannels;
	private long pupilId;
	private Switch sw;
	private List<Channel> trainerChannels;
	private long trainerId;
	private boolean wireless;

	public TrainerConfig() {
	}

	@XmlIDREF
	@XmlElement(name = "pupilChannel")
	public List<Channel> getPupilChannels() {
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
	public List<Channel> getTrainerChannels() {
		return trainerChannels;
	}

	public long getTrainerId() {
		return trainerId;
	}

	public boolean isWireless() {
		return wireless;
	}

	public void setPupilChannels(final List<Channel> pupilChannels) {
		this.pupilChannels = pupilChannels;
	}

	public void setPupilId(final long pupilId) {
		this.pupilId = pupilId;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}

	public void setTrainerChannels(final List<Channel> trainerChannels) {
		this.trainerChannels = trainerChannels;
	}

	public void setTrainerId(final long trainerId) {
		this.trainerId = trainerId;
	}

	public void setWireless(final boolean wireless) {
		this.wireless = wireless;
	}
}