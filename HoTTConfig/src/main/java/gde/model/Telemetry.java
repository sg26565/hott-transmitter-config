/**
 *  HoTT Transmitter Config
 *  Copyright (C) 2013  Oliver Treichel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package gde.model;

import gde.model.enums.SensorType;

import java.util.Collection;

/**
 * @author oli
 */
public class Telemetry {

	private SensorType							currentSensor;
	private int											currentSensorPage;
	private Collection<SensorType>	selectedSensor;
	private Switch									varioTone;
	private int											voiceDelay;
	private Switch									voiceRepeat;
	private Switch									voiceTrigger;

	public SensorType getCurrentSensor() {
		return currentSensor;
	}

	public int getCurrentSensorPage() {
		return currentSensorPage;
	}

	public Collection<SensorType> getSelectedSensor() {
		return selectedSensor;
	}

	public Switch getVarioTone() {
		return varioTone;
	}

	public int getVoiceDelay() {
		return voiceDelay;
	}

	public Switch getVoiceRepeat() {
		return voiceRepeat;
	}

	public Switch getVoiceTrigger() {
		return voiceTrigger;
	}

	public void setCurrentSensor(final SensorType currentSensor) {
		this.currentSensor = currentSensor;
	}

	public void setCurrentSensorPage(final int currentSensorPage) {
		this.currentSensorPage = currentSensorPage;
	}

	public void setSelectedSensor(final Collection<SensorType> selectedSensor) {
		this.selectedSensor = selectedSensor;
	}

	public void setVarioTone(final Switch sw) {
		varioTone = sw;
	}

	public void setVoiceDelay(final int voiceDelay) {
		this.voiceDelay = voiceDelay;
	}

	public void setVoiceRepeat(final Switch sw) {
		voiceRepeat = sw;
	}

	public void setVoiceTrigger(final Switch sw) {
		voiceTrigger = sw;
	}
}
