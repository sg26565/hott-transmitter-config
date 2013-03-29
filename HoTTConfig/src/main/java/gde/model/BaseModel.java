package gde.model;

import gde.model.container.Channels;
import gde.model.container.Clocks;
import gde.model.container.Controls;
import gde.model.container.Mixers;
import gde.model.container.Phases;
import gde.model.container.Switches;
import gde.model.enums.DSCOutputType;
import gde.model.enums.ExtPPMType;
import gde.model.enums.ModelType;
import gde.model.enums.SensorType;
import gde.model.enums.StickMode;
import gde.model.enums.TransmitterType;
import gde.model.enums.Vendor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
abstract public class BaseModel {
	private int appVersion;
	private boolean autoTimerReset;
	private Switch autoTrimSwitch;
	private boolean bound;
	private Channels channels;
	private Clocks clocks;
	private Controls controls;
	private DSCOutputType dscOutput;
	private ExtPPMType extPpmType;
	private int failSafeDelay;
	private boolean failSafeSettingCheck;
	private String info;
	private int memoryVersion;
	private Mixers mixers;
	private String modelName;
	private int modelNumber;
	private final ModelType modelType;
	private HFModule module;
	private Phases phases;
	private Switch powerOnWarning; // TODO check type
	private long receiverId;
	private SensorType displaySensorType;
	private SensorType settingSensorType;
	private int settingSensorPage;
	private StickMode stickMode;
	private Switches switches;
	private ThrottleCutOf throttleCutOf;
	private int throttleLastIdlePosition;
	private int throttleTrim;
	private TrainerConfig trainerConfig;
	private long transmitterId;
	private TransmitterType transmitterType;
	private Vendor vendor;
	private int voiceDelay;

	public BaseModel() {
		// required by JAXB
		this(ModelType.Unknown);
	}

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

	public Channels getChannels() {
		return channels;
	}

	public Clocks getClocks() {
		return clocks;
	}

	public Controls getControls() {
		return controls;
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

	@XmlElement(name = "freeMixers")
	public Mixers getMixers() {
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

	public Phases getPhases() {
		return phases;
	}

	@XmlIDREF
	public Switch getPowerOnWarning() {
		return powerOnWarning;
	}

	public long getReceiverId() {
		return receiverId;
	}

	public SensorType getDisplaySensorType() {
		return displaySensorType;
	}

	public StickMode getStickMode() {
		return stickMode;
	}

	public Switches getSwitches() {
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

	public void setChannels(final Channels channels) {
		this.channels = channels;
	}

	public void setClocks(final Clocks clocks) {
		this.clocks = clocks;
	}

	public void setControls(final Controls controls) {
		this.controls = controls;
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

	public void setMixers(final Mixers mixers) {
		this.mixers = mixers;
	}

	public void setModelName(final String modelName) {
		this.modelName = modelName;
	}

	public void setModelNumber(final int modelNumber) {
		this.modelNumber = modelNumber;
	}

	public void setModule(final HFModule module) {
		this.module = module;
	}

	public void setPhases(final Phases phases) {
		this.phases = phases;
	}

	public void setPowerOnWarning(final Switch powerOnWarning) {
		this.powerOnWarning = powerOnWarning;
	}

	public void setReceiverId(final long receiverId) {
		this.receiverId = receiverId;
	}

	public void setDisplaySensorType(final SensorType sensorType) {
		displaySensorType = sensorType;
	}

	public void setStickMode(final StickMode stickMode) {
		this.stickMode = stickMode;
	}

	public void setSwitches(final Switches switches) {
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

	public int getSettingSensorPage() {
		return settingSensorPage;
	}

	public void setSettingSensorPage(final int settingSensorPage) {
		this.settingSensorPage = settingSensorPage;
	}

	public SensorType getSettingSensorType() {
		return settingSensorType;
	}

	public void setSettingSensorType(final SensorType settingSensorType) {
		this.settingSensorType = settingSensorType;
	}
}
