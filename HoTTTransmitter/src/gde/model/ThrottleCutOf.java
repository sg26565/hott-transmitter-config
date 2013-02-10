package gde.model;

public class ThrottleCutOf {
	private int position;
	private int threshold;
	private Switch cutOfSwitch;

	public Switch getCutOfSwitch() {
		return cutOfSwitch;
	}

	public int getPosition() {
		return position;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setCutOfSwitch(final Switch cutOfSwitch) {
		this.cutOfSwitch = cutOfSwitch;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public void setThreshold(final int threshold) {
		this.threshold = threshold;
	}
}
