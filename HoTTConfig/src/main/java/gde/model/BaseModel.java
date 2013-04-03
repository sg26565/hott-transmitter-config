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
 *  along with this program.  If not, see <http://www.gnu.org/licenses/[].
 */
package gde.model;

import gde.model.enums.DSCOutputType;
import gde.model.enums.ExtPPMType;
import gde.model.enums.ModelType;
import gde.model.enums.SensorType;
import gde.model.enums.StickMode;
import gde.model.enums.SwitchFunction;
import gde.model.enums.TransmitterType;
import gde.model.enums.Vendor;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class BaseModel {
	private int appVersion;
	private boolean autoTimerReset;
	private Switch autoTrimSwitch;
	private boolean bound;
	private Channel[] channels;
	private Clock[] clocks;
	private SensorType currentSensor;
	private int currentSensorPage;
	private DSCOutputType dscOutput;
	private ExtPPMType extPpmType;
	private int failSafeDelay;
	private boolean failSafeSettingCheck;
	private String info;
	private int memoryVersion;
	private Mixer[] mixers;
	private String modelName;
	private int modelNumber;
	private ModelType modelType;
	private HFModule module;
	private Phase[] phases;
	private Switch powerOnWarning; // TODO check type
	private Receiver[] receivers;
	private Collection<SensorType> selectedSensors;
	private StickMode stickMode;
	private StickTrim[] stickTrim;
	private Switch[] switches;
	private ThrottleCutOf throttleCutOf;
	private int throttleLastIdlePosition;
	private int throttleTrim;
	private TrainerConfig trainerConfig;
	private long transmitterId;
	private TransmitterType transmitterType;
	private Vendor vendor;
	private int voiceDelay;

	public BaseModel(final ModelType modelType) {
		this.modelType = modelType;
	}

	public Switch get(final SwitchFunction function) {
		for (final Switch sw : getSwitches()) {
			if (sw.getFunction() == function) {
				return sw;
			}
		}

		return null;
	}

	public int getAppVersion() {
		return appVersion;
	}

	@XmlIDREF
	public Switch getAutoTrimSwitch() {
		return autoTrimSwitch;
	}

	@XmlElement(name = "channel")
	@XmlElementWrapper(name = "channels")
	public Channel[] getChannels() {
		return channels;
	}

	@XmlElement(name = "clock")
	@XmlElementWrapper(name = "clocks")
	public Clock[] getClocks() {
		return clocks;
	}

	public SensorType getCurrentSensor() {
		return currentSensor;
	}

	public int getCurrentSensorPage() {
		return currentSensorPage;
	}

	public DSCOutputType getDscOutput() {
		return dscOutput;
	}

	public ExtPPMType getExtPpmType() {
		return extPpmType;
	}

	public int getFailSafeDelay() {
		return failSafeDelay;
	}

	public String getInfo() {
		return info;
	}

	public int getMemoryVersion() {
		return memoryVersion;
	}

	@XmlElement(name = "freeMixer")
	@XmlElementWrapper(name = "freeMixers")
	public Mixer[] getMixers() {
		return mixers;
	}

	public String getModelName() {
		return modelName;
	}

	public int getModelNumber() {
		return modelNumber;
	}

	public ModelType getModelType() {
		return modelType;
	}

	public HFModule getModule() {
		return module;
	}

	@XmlElement(name = "phase")
	@XmlElementWrapper(name = "phases")
	public Phase[] getPhases() {
		return phases;
	}

	@XmlIDREF
	public Switch getPowerOnWarning() {
		return powerOnWarning;
	}

	@XmlElement(name = "receiver")
	@XmlElementWrapper(name = "receivers")
	public Receiver[] getReceivers() {
		return receivers;
	}

	public Collection<SensorType> getSelectedSensors() {
		return selectedSensors;
	}

	public StickMode getStickMode() {
		return stickMode;
	}

	@XmlElementWrapper(name = "sicktrims")
	public StickTrim[] getStickTrim() {
		return stickTrim;
	}

	@XmlElement(name = "switch")
	@XmlElementWrapper(name = "switches")
	public Switch[] getSwitches() {
		return switches;
	}

	public ThrottleCutOf getThrottleCutOf() {
		return throttleCutOf;
	}

	public int getThrottleLastIdlePosition() {
		return throttleLastIdlePosition;
	}

	public int getThrottleTrim() {
		return throttleTrim;
	}

	public TrainerConfig getTrainerConfig() {
		return trainerConfig;
	}

	public long getTransmitterId() {
		return transmitterId;
	}

	public TransmitterType getTransmitterType() {
		return transmitterType;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public int getVoiceDelay() {
		return voiceDelay;
	}

	public boolean isAutoTimerReset() {
		return autoTimerReset;
	}

	public boolean isBound() {
		return bound;
	}

	public boolean isFailSafeSettingCheck() {
		return failSafeSettingCheck;
	}

	public void setAppVersion(final int appVersion) {
		this.appVersion = appVersion;
	}

	public void setAutoTimerReset(final boolean autoTimerReset) {
		this.autoTimerReset = autoTimerReset;
	}

	public void setAutoTrimSwitch(final Switch autoTrimSwitch) {
		this.autoTrimSwitch = autoTrimSwitch;
	}

	public void setBound(final boolean bound) {
		this.bound = bound;
	}

	public void setChannels(final Channel[] channels) {
		this.channels = channels;
	}

	public void setClocks(final Clock[] clocks) {
		this.clocks = clocks;
	}

	public void setCurrentSensor(final SensorType settingSensorType) {
		currentSensor = settingSensorType;
	}

	public void setCurrentSensorPage(final int settingSensorPage) {
		currentSensorPage = settingSensorPage;
	}

	public void setDscOutput(final DSCOutputType dscOutput) {
		this.dscOutput = dscOutput;
	}

	public void setExtPpmType(final ExtPPMType extPpmType) {
		this.extPpmType = extPpmType;
	}

	public void setFailSafeDelay(final int failSafeDelay) {
		this.failSafeDelay = failSafeDelay;
	}

	public void setFailSafeSettingCheck(final boolean failSafeSettingCheck) {
		this.failSafeSettingCheck = failSafeSettingCheck;
	}

	public void setInfo(final String info) {
		this.info = info;
	}

	public void setMemoryVersion(final int memoryVersion) {
		this.memoryVersion = memoryVersion;
	}

	public void setMixers(final Mixer[] mixers) {
		this.mixers = mixers;
	}

	public void setModelName(final String modelName) {
		this.modelName = modelName;
	}

	public void setModelNumber(final int modelNumber) {
		this.modelNumber = modelNumber;
	}

	public void setModelType(final ModelType modelType) {
		this.modelType = modelType;
	}

	public void setModule(final HFModule module) {
		this.module = module;
	}

	public void setPhases(final Phase[] phases) {
		this.phases = phases;
	}

	public void setPowerOnWarning(final Switch powerOnWarning) {
		this.powerOnWarning = powerOnWarning;
	}

	public void setReceivers(final Receiver[] receivers) {
		this.receivers = receivers;
	}

	public void setSelectedSensors(final Collection<SensorType> selectedSensors) {
		this.selectedSensors = selectedSensors;
	}

	public void setStickMode(final StickMode stickMode) {
		this.stickMode = stickMode;
	}

	public void setStickTrim(final StickTrim[] stickTrim) {
		this.stickTrim = stickTrim;
	}

	public void setSwitches(final Switch[] switches) {
		this.switches = switches;
	}

	public void setThrottleCutOf(final ThrottleCutOf throttleCutOf) {
		this.throttleCutOf = throttleCutOf;
	}

	public void setThrottleLastIdlePosition(final int throttleLastPosition) {
		throttleLastIdlePosition = throttleLastPosition;
	}

	public void setThrottleTrim(final int throttleTrim) {
		this.throttleTrim = throttleTrim;
	}

	public void setTrainerConfig(final TrainerConfig trainerConfig) {
		this.trainerConfig = trainerConfig;
	}

	public void setTransmitterId(final long transmitterId) {
		this.transmitterId = transmitterId;
	}

	public void setTransmitterType(final TransmitterType transmitterType) {
		this.transmitterType = transmitterType;
	}

	public void setVendor(final Vendor vendor) {
		this.vendor = vendor;
	}

	public void setVoiceDelay(final int voiceDelay) {
		this.voiceDelay = voiceDelay;
	}
}
