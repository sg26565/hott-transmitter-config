package gde.model.helicopter;

import gde.model.PhasedTrim;

/**
 * @author oli@treichels.de
 */
public class HelicopterTrim extends PhasedTrim {
	private int nickStickTrim;
	private int rollStickTrim;
	private int tailStickTrim;

	public int getNickStickTrim() {
		return nickStickTrim;
	}

	public int getRollStickTrim() {
		return rollStickTrim;
	}

	public int getTailStickTrim() {
		return tailStickTrim;
	}

	public void setNickStickTrim(final int nickStickTrim) {
		this.nickStickTrim = nickStickTrim;
	}

	public void setRollStickTrim(final int rollStickTrim) {
		this.rollStickTrim = rollStickTrim;
	}

	public void setTailStickTrim(final int tailStickTrim) {
		this.tailStickTrim = tailStickTrim;
	}
}
