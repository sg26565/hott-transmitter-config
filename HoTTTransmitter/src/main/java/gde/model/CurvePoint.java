package gde.model;

public class CurvePoint {
	private boolean enabled;
	private int value;

	public int getValue() {
		return value;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public void setValue(final int value) {
		this.value = value;
	}
}
