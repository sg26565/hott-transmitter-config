package gde.model;

import gde.model.enums.SwitchType;

import javax.xml.bind.annotation.XmlID;

/**
 * @author oli@treichels.de
 */
public class Switch {
	private int assigment;
	private final String number;
	private int position;
	private SwitchType type;

	public Switch(final int number) {
		this.number = Integer.toString(number);
	}

	public int getAssigment() {
		return assigment;
	}

	@XmlID
	public String getNumber() {
		return number;
	}

	public int getPosition() {
		return position;
	}

	public SwitchType getType() {
		return type;
	}

	public void setAssigment(final int assigment) {
		this.assigment = assigment;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public void setType(final SwitchType type) {
		this.type = type;
	}
}
