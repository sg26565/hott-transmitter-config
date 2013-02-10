package gde.model;

public class Control {
	private int inputChannel;
	private ControlMode mode;

	public int getInputChannel() {
		return inputChannel;
	}

	public ControlMode getMode() {
		return mode;
	}

	public void setInputChannel(final int inputChannel) {
		this.inputChannel = inputChannel;
	}

	public void setMode(final ControlMode mode) {
		this.mode = mode;
	}
}
