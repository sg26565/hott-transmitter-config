/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.model;

import de.treichels.hott.model.enums.SensorType;
import de.treichels.hott.model.enums.TelemetryAlarmType;
import de.treichels.hott.model.enums.VarioToneSensor;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
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
    private VarioToneSensor varioToneSensor;
    private String[] userAlarmList;
    private int telemetryDataReceiveTime;
    private TelemetryAlarmType telemetryAlarmType;
    private int basicVoiceList;
    private int GeneralAirVoiceList;
    private int ElectricAirVoiceList;
    private int VarioVoiceList;
    private int GPSVoiceList;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Telemetry other = (Telemetry) obj;
        if (ElectricAirVoiceList != other.ElectricAirVoiceList) return false;
        if (GPSVoiceList != other.GPSVoiceList) return false;
        if (GeneralAirVoiceList != other.GeneralAirVoiceList) return false;
        if (VarioVoiceList != other.VarioVoiceList) return false;
        if (basicVoiceList != other.basicVoiceList) return false;
        if (currentSensor != other.currentSensor) return false;
        if (currentSensorPage != other.currentSensorPage) return false;
        if (selectedSensor == null) {
            if (other.selectedSensor != null) return false;
        } else if (!selectedSensor.equals(other.selectedSensor)) return false;
        if (telemetryAlarmType != other.telemetryAlarmType) return false;
        if (telemetryDataReceiveTime != other.telemetryDataReceiveTime) return false;
        if (!Arrays.equals(userAlarmList, other.userAlarmList)) return false;
        if (varioTone == null) {
            if (other.varioTone != null) return false;
        } else if (!varioTone.equals(other.varioTone)) return false;
        if (varioToneSensor != other.varioToneSensor) return false;
        if (voiceDelay != other.voiceDelay) return false;
        if (voiceRepeat == null) {
            if (other.voiceRepeat != null) return false;
        } else if (!voiceRepeat.equals(other.voiceRepeat)) return false;
        if (voiceTrigger == null) {
            return other.voiceTrigger == null;
        } else return voiceTrigger.equals(other.voiceTrigger);
    }

    public int getBasicVoiceList() {
        return basicVoiceList;
    }

    public SensorType getCurrentSensor() {
        return currentSensor;
    }

    public int getCurrentSensorPage() {
        return currentSensorPage;
    }

    public int getElectricAirVoiceList() {
        return ElectricAirVoiceList;
    }

    public int getGeneralAirVoiceList() {
        return GeneralAirVoiceList;
    }

    public int getGPSVoiceList() {
        return GPSVoiceList;
    }

    public Collection<SensorType> getSelectedSensor() {
        return selectedSensor;
    }

    public TelemetryAlarmType getTelemetryAlarmType() {
        return telemetryAlarmType;
    }

    public int getTelemetryDataReceiveTime() {
        return telemetryDataReceiveTime;
    }

    public String[] getUserAlarmList() {
        return userAlarmList;
    }

    public Switch getVarioTone() {
        return varioTone;
    }

    public VarioToneSensor getVarioToneSensor() {
        return varioToneSensor;
    }

    public int getVarioVoiceList() {
        return VarioVoiceList;
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
        result = prime * result + ElectricAirVoiceList;
        result = prime * result + GPSVoiceList;
        result = prime * result + GeneralAirVoiceList;
        result = prime * result + VarioVoiceList;
        result = prime * result + basicVoiceList;
        result = prime * result + (currentSensor == null ? 0 : currentSensor.hashCode());
        result = prime * result + currentSensorPage;
        result = prime * result + (selectedSensor == null ? 0 : selectedSensor.hashCode());
        result = prime * result + (telemetryAlarmType == null ? 0 : telemetryAlarmType.hashCode());
        result = prime * result + telemetryDataReceiveTime;
        result = prime * result + Arrays.hashCode(userAlarmList);
        result = prime * result + (varioTone == null ? 0 : varioTone.hashCode());
        result = prime * result + (varioToneSensor == null ? 0 : varioToneSensor.hashCode());
        result = prime * result + voiceDelay;
        result = prime * result + (voiceRepeat == null ? 0 : voiceRepeat.hashCode());
        result = prime * result + (voiceTrigger == null ? 0 : voiceTrigger.hashCode());
        return result;
    }

    public void setBasicVoiceList(final int basicVoiceList) {
        this.basicVoiceList = basicVoiceList;
    }

    public void setCurrentSensor(final SensorType currentSensor) {
        this.currentSensor = currentSensor;
    }

    public void setCurrentSensorPage(final int currentSensorPage) {
        this.currentSensorPage = currentSensorPage;
    }

    public void setElectricAirVoiceList(final int electricAirVoiceList) {
        ElectricAirVoiceList = electricAirVoiceList;
    }

    public void setGeneralAirVoiceList(final int generalAirVoiceList) {
        GeneralAirVoiceList = generalAirVoiceList;
    }

    public void setGPSVoiceList(final int gPSVoiceList) {
        GPSVoiceList = gPSVoiceList;
    }

    public void setSelectedSensor(final Collection<SensorType> selectedSensor) {
        this.selectedSensor = selectedSensor;
    }

    public void setTelemetryAlarmType(final TelemetryAlarmType telemetryAlarmType) {
        this.telemetryAlarmType = telemetryAlarmType;
    }

    public void setTelemetryDataReceiveTime(final int telemetryDataReceiveTime) {
        this.telemetryDataReceiveTime = telemetryDataReceiveTime;
    }

    public void setUserAlarmList(final String[] userAlarmList) {
        this.userAlarmList = userAlarmList;
    }

    public void setVarioTone(final Switch sw) {
        varioTone = sw;
    }

    public void setVarioToneSensor(final VarioToneSensor varioToneSensor) {
        this.varioToneSensor = varioToneSensor;
    }

    public void setVarioVoiceList(final int varioVoiceList) {
        VarioVoiceList = varioVoiceList;
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
