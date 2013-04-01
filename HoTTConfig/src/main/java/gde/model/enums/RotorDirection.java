package gde.model.enums;

import java.util.ResourceBundle;

/**
 * @author oli@treichels.de
 */
public enum RotorDirection {
	Left, Right;

	/** @return the locale-dependent message */
	@Override
	public String toString() {
		return ResourceBundle.getBundle(getClass().getName()).getString(name());
	}
}
