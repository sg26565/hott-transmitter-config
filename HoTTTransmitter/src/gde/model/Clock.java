package gde.model;

public class Clock {
	private String name;
	private ClockType type;
	private int value;
	private Switch sw;

	public String getName() {
		return name;
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

	public void setName(final String name) {
		this.name = name;
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

	public int getMinutes() {
		return getValue() / 60;
	}

	public int getSeconds() {
		return getValue() % 60;
	}

	public void setMinutes(final int minutes) {
		setValue(minutes * 60 + getSeconds());
	}

	public void setSeconds(final int seconds) {
		setValue(getMinutes() * 60 + seconds);
	}
}
