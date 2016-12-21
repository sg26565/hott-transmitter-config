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

import java.util.Collection;

import gde.model.enums.SensorType;

/**
 * @author oli
 */
public class Telemetry extends AbstractBase {
	private static final long serialVersionUID = 1L;

	private SensorType currentSensor;
	private int currentSensorPage;
	private Collection<SensorType> selectedSensor;
	private Switch varioTone;
	private int voiceDelay;
	private Switch voiceRepeat;
	private Switch voiceTrigger;

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Telemetry other = (Telemetry) obj;
		if (currentSensor != other.currentSensor) {
			return false;
		}
		if (currentSensorPage != other.currentSensorPage) {
			return false;
		}
		if (selectedSensor == null) {
			if (other.selectedSensor != null) {
				return false;
			}
		} else if (!selectedSensor.equals(other.selectedSensor)) {
			return false;
		}
		if (varioTone == null) {
			if (other.varioTone != null) {
				return false;
			}
		} else if (!varioTone.equals(other.varioTone)) {
			return false;
		}
		if (voiceDelay != other.voiceDelay) {
			return false;
		}
		if (voiceRepeat == null) {
			if (other.voiceRepeat != null) {
				return false;
			}
		} else if (!voiceRepeat.equals(other.voiceRepeat)) {
			return false;
		}
		if (voiceTrigger == null) {
			if (other.voiceTrigger != null) {
				return false;
			}
		} else if (!voiceTrigger.equals(other.voiceTrigger)) {
			return false;
		}
		return true;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (currentSensor == null ? 0 : currentSensor.hashCode());
		result = prime * result + currentSensorPage;
		result = prime * result + (selectedSensor == null ? 0 : selectedSensor.hashCode());
		result = prime * result + (varioTone == null ? 0 : varioTone.hashCode());
		result = prime * result + voiceDelay;
		result = prime * result + (voiceRepeat == null ? 0 : voiceRepeat.hashCode());
		result = prime * result + (voiceTrigger == null ? 0 : voiceTrigger.hashCode());
		return result;
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
