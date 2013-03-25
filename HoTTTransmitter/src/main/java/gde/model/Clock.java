package gde.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Clock {
	private String name;
	private Switch sw;
	private ClockType type;
	private int value;
	@XmlAttribute
	private final int number;

	public Clock(final int number) {
		this.number = number;
	}

	public int getMinutes() {
		return getValue() / 60;
	}

	public String getName() {
		return name;
	}

	public int getSeconds() {
		return getValue() % 60;
	}

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

	public int getNumber() {
		return number;
	}
}
