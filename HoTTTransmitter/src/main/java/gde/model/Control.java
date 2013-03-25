package gde.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Control {
	@XmlAttribute
	private final int number;
	private ControlMode mode;
	private int travelHigh;
	private int travelLow;
	private Switch sw;

	public Control(final int number) {
		this.number = number;
	}

	public ControlMode getMode() {
		return mode;
	}

	public int getTravelHigh() {
		return travelHigh;
	}

	public int getTravelLow() {
		return travelLow;
	}

	public void setMode(final ControlMode mode) {
		this.mode = mode;
	}

	public void setTravelHigh(final int travelHigh) {
		this.travelHigh = travelHigh;
	}

	public void setTravelLow(final int travelLow) {
		this.travelLow = travelLow;
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
