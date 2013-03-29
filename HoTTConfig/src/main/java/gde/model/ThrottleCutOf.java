package gde.model;

import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class ThrottleCutOf {
	private int position;
	private Switch sw;
	private int threshold;

	public int getPosition() {
		return position;
	}

	@XmlIDREF
	public Switch getSwitch() {
		return sw;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}

	public void setThreshold(final int threshold) {
		this.threshold = threshold;
	}
}
