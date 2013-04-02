package gde.model;

import gde.model.enums.ClockType;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class Clock {
	private String name;
	private String number;
	private Switch sw;
	private ClockType type;
	private int value;

	public Clock(final int number) {
		this.number = Integer.toString(number);
	}

	public int getMinutes() {
		return getValue() / 60;
	}

	public String getName() {
		return name;
	}

	@XmlAttribute
	@XmlID
	public String getNumber() {
		return number;
	}

	public int getSeconds() {
		return getValue() % 60;
	}

	@XmlIDREF
	public Switch getSwitch() {
		return sw;
	}

	public ClockType getType() {
		return type;
	}

	public int getValue() {
		return value;
	}

	public void setMinutes(final int minutes) {
		setValue(minutes * 60 + getSeconds());
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public void setSeconds(final int seconds) {
		setValue(getMinutes() * 60 + seconds);
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}

	public void setType(final ClockType type) {
		this.type = type;
	}

	public void setValue(final int value) {
		this.value = value;
	}
}
