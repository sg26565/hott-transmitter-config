package gde.model.enums;

import java.util.ResourceBundle;

/**
 * @author oli@treichels.de
 */
public enum SwashplateType {
	FourServo, OneServo, ThreeServo140, ThreeServoNick, ThreeServoRoll, TwoServo;

	/** @return the locale-dependent message */
	@Override
	public String toString() {
		return ResourceBundle.getBundle(getClass().getName()).getString(name());
	}
}
