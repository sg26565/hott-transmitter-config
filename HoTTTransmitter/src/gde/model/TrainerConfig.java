package gde.model;

import java.util.List;

public class TrainerConfig {
	private boolean wireless;
	private long trainerId;
	private long pupilId;
	private List<Channel> trainerChannels;
	private List<Channel> pupilChannels;
	private Switch sw;

	public TrainerConfig() {
	}

	public boolean isWireless() {
		return wireless;
	}

	public void setWireless(final boolean wireless) {
		this.wireless = wireless;
	}

	public long getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(final long trainerId) {
		this.trainerId = trainerId;
	}

	public long getPupilId() {
		return pupilId;
	}

	public void setPupilId(final long pupilId) {
		this.pupilId = pupilId;
	}

	public List<Channel> getPupilChannels() {
		return pupilChannels;
	}

	public void setPupilChannels(final List<Channel> pupilChannels) {
		this.pupilChannels = pupilChannels;
	}

	public List<Channel> getTrainerChannels() {
		return trainerChannels;
	}

	public void setTrainerChannels(final List<Channel> trainerChannels) {
		this.trainerChannels = trainerChannels;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}

	public Switch getSwitch() {
		return sw;
	}
}