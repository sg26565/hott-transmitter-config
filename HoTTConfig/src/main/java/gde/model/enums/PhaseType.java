package gde.model.enums;

import java.util.ResourceBundle;

/**
 * @author oli@treichels.de
 */
public enum PhaseType {
	Acro, Acro_3D, AirTow, Autorotation, Distance, Hover, Landing, Normal, Speed, TakeOff, Test, Thermal, Global;

	/** @return the locale-dependent message */
	@Override
	public String toString() {
		return ResourceBundle.getBundle(getClass().getName()).getString(name());
	}
}
