package gde.model;


public class ThrottleCutOf {
	private Switch sw;
	private int position;
	private int threshold;

	public Switch getSwitch() {
		return sw;
	}

	public int getPosition() {
		return position;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public void setThreshold(final int threshold) {
		this.threshold = threshold;
	}
}
