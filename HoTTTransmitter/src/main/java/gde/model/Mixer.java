package gde.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Mixer {
	@XmlAttribute
	private final int number;
	private Channel fromChannel;
	private MixerInputType inputType;
	private int offset;
	private Channel toChannel;
	private int travelHigh;
	private int travelLow;
	private MixerType type;
	private Switch sw;

	public Mixer(final int number) {
		this.number = number;
	}

	public Channel getFromChannel() {
		return fromChannel;
	}

	public MixerInputType getInputType() {
		return inputType;
	}

	public int getOffset() {
		return offset;
	}

	public Channel getToChannel() {
		return toChannel;
	}

	public int getTravelHigh() {
		return travelHigh;
	}

	public int getTravelLow() {
		return travelLow;
	}

	public MixerType getType() {
		return type;
	}

	public void setFromChannel(final Channel from) {
		this.fromChannel = from;
	}

	public void setInputType(final MixerInputType inputType) {
		this.inputType = inputType;
	}

	public void setOffset(final int offset) {
		this.offset = offset;
	}

	public void setToChannel(final Channel to) {
		this.toChannel = to;
	}

	public void setTravelHigh(final int travelHigh) {
		this.travelHigh = travelHigh;
	}

	public void setTravelLow(final int travelLow) {
		this.travelLow = travelLow;
	}

	public void setType(final MixerType type) {
		this.type = type;
	}

	public int getNumber() {
		return number;
	}

	public Switch getSwitch() {
		return sw;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}
}
