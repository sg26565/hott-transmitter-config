package gde.model;

public class Switch {
	private final SwitchFunction function;
	private SwitchType type;
	private int assigment;
	private int position;

	public Switch(final SwitchFunction function) {
		this.function = function;
	}

	public int getAssigment() {
		return assigment;
	}

	public int getPosition() {
		return position;
	}

	public void setAssigment(final int assigment) {
		this.assigment = assigment;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public SwitchFunction getFunction() {
		return function;
	}

	public SwitchType getType() {
		return type;
	}

	public void setType(final SwitchType type) {
		this.type = type;
	}
}
