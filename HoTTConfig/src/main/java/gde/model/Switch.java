package gde.model;

import gde.model.enums.SwitchFunction;
import gde.model.enums.SwitchType;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

/**
 * @author oli@treichels.de
 */
public class Switch {
	private int assignment;
	private final SwitchFunction function;
	private int position;
	private SwitchType type;

	public Switch(final SwitchFunction function) {
		this.function = function;
	}

	public int getAssignment() {
		return assignment;
	}

	public int getPosition() {
		return position;
	}

	public SwitchType getType() {
		return type;
	}

	public void setAssignment(final int assigment) {
		assignment = assigment;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public void setType(final SwitchType type) {
		this.type = type;
	}

	@XmlAttribute
	@XmlID
	public String getFunction() {
		return function.toString();
	}
}
