package gde.model.enums;

import java.util.ResourceBundle;

/**
 * @author oli@treichels.de
 */
public enum TrainerMode {
	Pupil, Trainer;

	/** @return the locale-dependent message */
	@Override
	public String toString() {
		return ResourceBundle.getBundle(getClass().getName()).getString(name());
	}
}
