package gde.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class Control {
	private ControlMode mode;
	private final String number;
	private Switch sw;
	private int travelHigh;
	private int travelLow;

	public Control(final int number) {
		this.number = Integer.toString(number);
	}

	public ControlMode getMode() {
		return mode;
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

	public int getTravelHigh() {
		return travelHigh;
	}

	public int getTravelLow() {
		return travelLow;
	}

	public void setMode(final ControlMode mode) {
		this.mode = mode;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}

	public void setTravelHigh(final int travelHigh) {
		this.travelHigh = travelHigh;
	}

	public void setTravelLow(final int travelLow) {
		this.travelLow = travelLow;
	}
}
