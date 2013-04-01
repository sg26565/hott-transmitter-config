package gde.model;

import gde.model.enums.SwitchFunction;
import gde.model.enums.SwitchName;
import gde.model.enums.SwitchType;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

/**
 * @author oli@treichels.de
 */
public class Switch {
	private static int NEXT_NUMBER = 0;

	private SwitchName assignment;
	private final SwitchFunction function;
	private int position;
	private SwitchType type;
	private final String number;

	public Switch(final SwitchFunction function) {
		this.function = function;
		number = Integer.toString(NEXT_NUMBER++);
	}

	public SwitchName getAssignment() {
		return assignment;
	}

	public int getPosition() {
		return position;
	}

	public SwitchType getType() {
		return type;
	}

	public void setAssignment(final SwitchName assigment) {
		assignment = assigment;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public void setType(final SwitchType type) {
		this.type = type;
	}

	public SwitchFunction getFunction() {
		return function;
	}

	@XmlAttribute
	@XmlID
	public String getNumber() {
		return number;
	}
}
