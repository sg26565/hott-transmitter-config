package gde.model.enums;

import java.util.ResourceBundle;

/**
 * @author oli@treichels.de
 */
public enum PhaseType {
	Acro, Acro_3D, AirTow, Autorotation, Distance, Global, Hover, Landing, Normal, Speed, TakeOff, Test, Thermal;

	/** @return the locale-dependent message */
	@Override
	public String toString() {
		return ResourceBundle.getBundle(getClass().getName()).getString(name());
	}
}
