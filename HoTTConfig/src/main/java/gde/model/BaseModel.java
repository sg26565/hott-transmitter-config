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
import gde.model.enums.TransmitterType;
import gde.model.enums.Vendor;

import java.util.Collection;

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
	private Channel[] channel;
	private ChannelMapping[] channelMapping;
	private ChannelSequencer channelSequencer;
	private Clock[] clock;
	private ControlSwitch[] controlSwitch;
	private SensorType currentSensor;
	private int currentSensorPage;
	private DSCOutputType dscOutputType;
	private DualMixer[] dualMixer;
	private ExtPPMType extPpmType;
	private double failSafeDelay;
	private boolean failSafeSettingCheck;
	private FreeMixer[] freeMixer;
	private String info;
	private LogicalSwitch[] logicalSwitch;
	private int memoryVersion;
	private String modelName;
	private int modelNumber;
	private ModelType modelType;
	private HFModule module;
	private Multichannel[] multichannel;
	private Phase[] phase;
	private PhaseAssignment phaseAssignment;
	private Switch powerOnWarning; // TODO check type
	private Receiver[] receiver;
	private Collection<SensorType> selectedSensor;
	private StickMode stickMode;
	private StickTrim[] stickTrim;
	private Switch[] sw;
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

	public int getAppVersion() {
		return appVersion;
	}

	@XmlIDREF
	public Switch getAutoTrimSwitch() {
		return autoTrimSwitch;
	}

	@XmlElementWrapper(name = "channels")
	public Channel[] getChannel() {
		return channel;
	}

	@XmlElementWrapper(name = "channelMappings")
	public ChannelMapping[] getChannelMapping() {
		return channelMapping;
	}

	public ChannelSequencer getChannelSequencer() {
		return channelSequencer;
	}

	@XmlElementWrapper(name = "clocks")
	public Clock[] getClock() {
		return clock;
	}

	@XmlElementWrapper(name = "controlSwitches")
	@XmlIDREF
	public ControlSwitch[] getControlSwitch() {
		return controlSwitch;
	}

	public SensorType getCurrentSensor() {
		return currentSensor;
	}

	public int getCurrentSensorPage() {
		return currentSensorPage;
	}

	public DSCOutputType getDscOutputType() {
		return dscOutputType;
	}

	@XmlElementWrapper(name = "dualMixers")
	public DualMixer[] getDualMixer() {
		return dualMixer;
	}

	public ExtPPMType getExtPpmType() {
		return extPpmType;
	}

	public double getFailSafeDelay() {
		return failSafeDelay;
	}

	@XmlElementWrapper(name = "freeMixers")
	public FreeMixer[] getFreeMixer() {
		return freeMixer;
	}

	public String getInfo() {
		return info;
	}

	@XmlElementWrapper(name = "logicalSwitches")
	@XmlIDREF
	public LogicalSwitch[] getLogicalSwitch() {
		return logicalSwitch;
	}

	public int getMemoryVersion() {
		return memoryVersion;
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

	@XmlElementWrapper(name = "multichannels")
	public Multichannel[] getMultichannel() {
		return multichannel;
	}

	@XmlElementWrapper(name = "phases")
	public Phase[] getPhase() {
		return phase;
	}

	public PhaseAssignment getPhaseAssignment() {
		return phaseAssignment;
	}

	@XmlIDREF
	public Switch getPowerOnWarning() {
		return powerOnWarning;
	}

	@XmlElementWrapper(name = "receivers")
	public Receiver[] getReceiver() {
		return receiver;
	}

	public Collection<SensorType> getSelectedSensor() {
		return selectedSensor;
	}

	public StickMode getStickMode() {
		return stickMode;
	}

	@XmlElementWrapper(name = "sicktrims")
	public StickTrim[] getStickTrim() {
		return stickTrim;
	}

	@XmlElementWrapper(name = "switches")
	public Switch[] getSwitch() {
		return sw;
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

	public void setChannel(final Channel[] channels) {
		channel = channels;
	}

	public void setChannelMapping(final ChannelMapping[] channelMapping) {
		this.channelMapping = channelMapping;
	}

	public void setChannelSequencer(final ChannelSequencer channelSequencer) {
		this.channelSequencer = channelSequencer;
	}

	public void setClock(final Clock[] clocks) {
		clock = clocks;
	}

	public void setControlSwitch(final ControlSwitch[] controlSwitches) {
		controlSwitch = controlSwitches;
	}

	public void setCurrentSensor(final SensorType settingSensorType) {
		currentSensor = settingSensorType;
	}

	public void setCurrentSensorPage(final int settingSensorPage) {
		currentSensorPage = settingSensorPage;
	}

	public void setDscOutputType(final DSCOutputType dscOutput) {
		dscOutputType = dscOutput;
	}

	public void setDualMixer(final DualMixer[] dualMixer) {
		this.dualMixer = dualMixer;
	}

	public void setExtPpmType(final ExtPPMType extPpmType) {
		this.extPpmType = extPpmType;
	}

	public void setFailSafeDelay(final double d) {
		failSafeDelay = d;
	}

	public void setFailSafeSettingCheck(final boolean failSafeSettingCheck) {
		this.failSafeSettingCheck = failSafeSettingCheck;
	}

	public void setFreeMixer(final FreeMixer[] freeMixer) {
		this.freeMixer = freeMixer;
	}

	public void setInfo(final String info) {
		this.info = info;
	}

	public void setLogicalSwitch(final LogicalSwitch[] logicalSwitches) {
		logicalSwitch = logicalSwitches;
	}

	public void setMemoryVersion(final int memoryVersion) {
		this.memoryVersion = memoryVersion;
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

	public void setMultichannel(final Multichannel[] multichannel) {
		this.multichannel = multichannel;
	}

	public void setPhase(final Phase[] phases) {
		phase = phases;
	}

	public void setPhaseAssignment(final PhaseAssignment phaseAssignment) {
		this.phaseAssignment = phaseAssignment;
	}

	public void setPowerOnWarning(final Switch powerOnWarning) {
		this.powerOnWarning = powerOnWarning;
	}

	public void setReceiver(final Receiver[] receivers) {
		receiver = receivers;
	}

	public void setSelectedSensor(final Collection<SensorType> selectedSensors) {
		selectedSensor = selectedSensors;
	}

	public void setStickMode(final StickMode stickMode) {
		this.stickMode = stickMode;
	}

	public void setStickTrim(final StickTrim[] stickTrim) {
		this.stickTrim = stickTrim;
	}

	public void setSwitch(final Switch[] switches) {
		sw = switches;
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
