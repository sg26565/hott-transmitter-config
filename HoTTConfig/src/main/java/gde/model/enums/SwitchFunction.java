package gde.model.enums;

import java.util.ResourceBundle;

/**
 * @author oli@treichels.de
 */
public enum SwitchFunction {
	Autorotation, Clock, CutOff, Diff_Aileron, Diff_Flap, Dr_Expo_Aileron, Dr_Expo_Elevator, Dr_Expo_Rudder, FreeMixer1, FreeMixer2, FreeMixer3, InputControl5, InputControl6, InputControl7, InputControl8, Mix_Aileron_Flap, Mix_Aileron_Rudder, Mix_Brake_Aileron, Mix_Brake_Elevator, Mix_Brake_Flap, Mix_Elevator_Aileron, Mix_Elevator_Flap, Mix_Flap_Aileron, Mix_Flap_Elevator, Phase2, Phase3, Phase4, ThrottleLimit, Trainer, VarioTone, VoiceRepeat, VoiceTrigger, Diff_Reduction;

	/** @return the locale-dependent message */
	@Override
	public String toString() {
		return ResourceBundle.getBundle(getClass().getName()).getString(name());
	}
}
