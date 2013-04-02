package gde.model;

import gde.model.enums.MixerInputType;
import gde.model.enums.MixerType;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class Mixer {
	private Channel fromChannel;
	private MixerInputType inputType;
	private String number;
	private int offset;
	private Switch sw;
	private Channel toChannel;
	private int travelHigh;
	private int travelLow;
	private MixerType type;

	public Mixer(final int number) {
		this.number = Integer.toString(number);
	}

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

	public int getOffset() {
		return offset;
	}

	@XmlIDREF
	public Switch getSwitch() {
		return sw;
	}

	@XmlIDREF
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
		fromChannel = from;
	}

	public void setInputType(final MixerInputType inputType) {
		this.inputType = inputType;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public void setOffset(final int offset) {
		this.offset = offset;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}

	public void setToChannel(final Channel to) {
		toChannel = to;
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
}
