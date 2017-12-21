/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/[].
 */
package de.treichels.hott.model;

import de.treichels.hott.model.enums.*;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import java.util.Arrays;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class BaseModel extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private int appVersion;
    private boolean autoTimerReset;
    private Switch autoTrimSwitch;
    private boolean bound;
    private Channel[] channel;
    private ChannelMapping[] channelMapping;
    private ChannelSequencer channelSequencer;
    private Clock[] clock;
    private ControlSwitch[] controlSwitch;
    private DSCOutputType dscOutputType;
    private DualMixer[] dualMixer;
    private ExtPPMType extPpmType;
    private double failSafeDelay;
    private boolean failSafeSettingCheck;
    private FreeMixer[] freeMixer;
    private LogicalSwitch[] logicalSwitch;
    private int memoryVersion;
    private String modelInfo;
    private String modelName;
    private int modelNumber;
    private ModelType modelType;
    private HFModule module;
    private Mp3Player mp3Player;
    private Multichannel[] multichannel;
    private Phase[] phase;
    private PhaseAssignment phaseAssignment;
    private Receiver[] receiver;
    private ReceiverType receiverType;
    private RingLimiter[] ringLimiter;
    private StickMode stickMode;
    private StickTrim[] stickTrim;
    private Switch[] sw;
    private Telemetry telemetry;
    private ThrottleSettings throttleSettings;
    private TrainerConfig trainerConfig;
    private long transmitterId;
    private TransmitterType transmitterType;
    private Vendor vendor;
    private int spektrumChannelNumber;
    private SpektrumMode spektrumMode;
    private int[] volumeTrim;
    private int[] globalTrimValue;
    private int[] globalTrimStep;
    private LapStore lapStore;
    private ReceiverBindType receiverBindType;
    private SwitchAnnouncement[] switchAnnouncements;
    private long escVoiceAnnounceFlag;

    public BaseModel() {
        modelType = ModelType.Unknown;
    }

    public BaseModel(final ModelType modelType) {
        this.modelType = modelType;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final BaseModel other = (BaseModel) obj;
        if (appVersion != other.appVersion) return false;
        if (autoTimerReset != other.autoTimerReset) return false;
        if (autoTrimSwitch == null) {
            if (other.autoTrimSwitch != null) return false;
        } else if (!autoTrimSwitch.equals(other.autoTrimSwitch)) return false;
        if (bound != other.bound) return false;
        if (!Arrays.equals(channel, other.channel)) return false;
        if (!Arrays.equals(channelMapping, other.channelMapping)) return false;
        if (channelSequencer == null) {
            if (other.channelSequencer != null) return false;
        } else if (!channelSequencer.equals(other.channelSequencer)) return false;
        if (!Arrays.equals(clock, other.clock)) return false;
        if (!Arrays.equals(controlSwitch, other.controlSwitch)) return false;
        if (dscOutputType != other.dscOutputType) return false;
        if (!Arrays.equals(dualMixer, other.dualMixer)) return false;
        if (escVoiceAnnounceFlag != other.escVoiceAnnounceFlag) return false;
        if (extPpmType != other.extPpmType) return false;
        if (Double.doubleToLongBits(failSafeDelay) != Double.doubleToLongBits(other.failSafeDelay)) return false;
        if (failSafeSettingCheck != other.failSafeSettingCheck) return false;
        if (!Arrays.equals(freeMixer, other.freeMixer)) return false;
        if (!Arrays.equals(globalTrimStep, other.globalTrimStep)) return false;
        if (!Arrays.equals(globalTrimValue, other.globalTrimValue)) return false;
        if (lapStore == null) {
            if (other.lapStore != null) return false;
        } else if (!lapStore.equals(other.lapStore)) return false;
        if (!Arrays.equals(logicalSwitch, other.logicalSwitch)) return false;
        if (memoryVersion != other.memoryVersion) return false;
        if (modelInfo == null) {
            if (other.modelInfo != null) return false;
        } else if (!modelInfo.equals(other.modelInfo)) return false;
        if (modelName == null) {
            if (other.modelName != null) return false;
        } else if (!modelName.equals(other.modelName)) return false;
        if (modelNumber != other.modelNumber) return false;
        if (modelType != other.modelType) return false;
        if (module == null) {
            if (other.module != null) return false;
        } else if (!module.equals(other.module)) return false;
        if (mp3Player == null) {
            if (other.mp3Player != null) return false;
        } else if (!mp3Player.equals(other.mp3Player)) return false;
        if (!Arrays.equals(multichannel, other.multichannel)) return false;
        if (!Arrays.equals(phase, other.phase)) return false;
        if (phaseAssignment == null) {
            if (other.phaseAssignment != null) return false;
        } else if (!phaseAssignment.equals(other.phaseAssignment)) return false;
        if (!Arrays.equals(receiver, other.receiver)) return false;
        if (receiverBindType != other.receiverBindType) return false;
        if (receiverType != other.receiverType) return false;
        if (!Arrays.equals(ringLimiter, other.ringLimiter)) return false;
        if (spektrumChannelNumber != other.spektrumChannelNumber) return false;
        if (spektrumMode != other.spektrumMode) return false;
        if (stickMode != other.stickMode) return false;
        if (!Arrays.equals(stickTrim, other.stickTrim)) return false;
        if (!Arrays.equals(sw, other.sw)) return false;
        if (!Arrays.equals(switchAnnouncements, other.switchAnnouncements)) return false;
        if (telemetry == null) {
            if (other.telemetry != null) return false;
        } else if (!telemetry.equals(other.telemetry)) return false;
        if (throttleSettings == null) {
            if (other.throttleSettings != null) return false;
        } else if (!throttleSettings.equals(other.throttleSettings)) return false;
        if (trainerConfig == null) {
            if (other.trainerConfig != null) return false;
        } else if (!trainerConfig.equals(other.trainerConfig)) return false;
        if (transmitterId != other.transmitterId) return false;
        if (transmitterType != other.transmitterType) return false;
        if (vendor != other.vendor) return false;
        return Arrays.equals(volumeTrim, other.volumeTrim);
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

    public DSCOutputType getDscOutputType() {
        return dscOutputType;
    }

    @XmlElementWrapper(name = "dualMixers")
    public DualMixer[] getDualMixer() {
        return dualMixer;
    }

    public long getEscVoiceAnnounceFlag() {
        return escVoiceAnnounceFlag;
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

    public int[] getGlobalTrimStep() {
        return globalTrimStep;
    }

    public int[] getGlobalTrimValue() {
        return globalTrimValue;
    }

    public LapStore getLapStore() {
        return lapStore;
    }

    @XmlElementWrapper(name = "logicalSwitches")
    @XmlIDREF
    public LogicalSwitch[] getLogicalSwitch() {
        return logicalSwitch;
    }

    public int getMemoryVersion() {
        return memoryVersion;
    }

    public String getModelInfo() {
        return modelInfo;
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

    public Mp3Player getMp3Player() {
        return mp3Player;
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

    @XmlElementWrapper(name = "receivers")
    public Receiver[] getReceiver() {
        return receiver;
    }

    public ReceiverBindType getReceiverBindType() {
        return receiverBindType;
    }

    public ReceiverType getReceiverType() {
        return receiverType;
    }

    @XmlElementWrapper(name = "ringLimiters")
    public RingLimiter[] getRingLimiter() {
        return ringLimiter;
    }

    public int getSpektrumChannelNumber() {
        return spektrumChannelNumber;
    }

    public SpektrumMode getSpektrumMode() {
        return spektrumMode;
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

    public Switch getSwitch(final String id) {
        for (final Switch sw : getSwitch())
            if (sw.getId().equals(id)) return sw;

        return null;
    }

    public SwitchAnnouncement[] getSwitchAnnouncements() {
        return switchAnnouncements;
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }

    public ThrottleSettings getThrottleSettings() {
        return throttleSettings;
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

    public int[] getVolumeTrim() {
        return volumeTrim;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + appVersion;
        result = prime * result + (autoTimerReset ? 1231 : 1237);
        result = prime * result + (autoTrimSwitch == null ? 0 : autoTrimSwitch.hashCode());
        result = prime * result + (bound ? 1231 : 1237);
        result = prime * result + Arrays.hashCode(channel);
        result = prime * result + Arrays.hashCode(channelMapping);
        result = prime * result + (channelSequencer == null ? 0 : channelSequencer.hashCode());
        result = prime * result + Arrays.hashCode(clock);
        result = prime * result + Arrays.hashCode(controlSwitch);
        result = prime * result + (dscOutputType == null ? 0 : dscOutputType.hashCode());
        result = prime * result + Arrays.hashCode(dualMixer);
        result = prime * result + (int) (escVoiceAnnounceFlag ^ escVoiceAnnounceFlag >>> 32);
        result = prime * result + (extPpmType == null ? 0 : extPpmType.hashCode());
        long temp;
        temp = Double.doubleToLongBits(failSafeDelay);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + (failSafeSettingCheck ? 1231 : 1237);
        result = prime * result + Arrays.hashCode(freeMixer);
        result = prime * result + Arrays.hashCode(globalTrimStep);
        result = prime * result + Arrays.hashCode(globalTrimValue);
        result = prime * result + (lapStore == null ? 0 : lapStore.hashCode());
        result = prime * result + Arrays.hashCode(logicalSwitch);
        result = prime * result + memoryVersion;
        result = prime * result + (modelInfo == null ? 0 : modelInfo.hashCode());
        result = prime * result + (modelName == null ? 0 : modelName.hashCode());
        result = prime * result + modelNumber;
        result = prime * result + (modelType == null ? 0 : modelType.hashCode());
        result = prime * result + (module == null ? 0 : module.hashCode());
        result = prime * result + (mp3Player == null ? 0 : mp3Player.hashCode());
        result = prime * result + Arrays.hashCode(multichannel);
        result = prime * result + Arrays.hashCode(phase);
        result = prime * result + (phaseAssignment == null ? 0 : phaseAssignment.hashCode());
        result = prime * result + Arrays.hashCode(receiver);
        result = prime * result + (receiverBindType == null ? 0 : receiverBindType.hashCode());
        result = prime * result + (receiverType == null ? 0 : receiverType.hashCode());
        result = prime * result + Arrays.hashCode(ringLimiter);
        result = prime * result + spektrumChannelNumber;
        result = prime * result + (spektrumMode == null ? 0 : spektrumMode.hashCode());
        result = prime * result + (stickMode == null ? 0 : stickMode.hashCode());
        result = prime * result + Arrays.hashCode(stickTrim);
        result = prime * result + Arrays.hashCode(sw);
        result = prime * result + Arrays.hashCode(switchAnnouncements);
        result = prime * result + (telemetry == null ? 0 : telemetry.hashCode());
        result = prime * result + (throttleSettings == null ? 0 : throttleSettings.hashCode());
        result = prime * result + (trainerConfig == null ? 0 : trainerConfig.hashCode());
        result = prime * result + (int) (transmitterId ^ transmitterId >>> 32);
        result = prime * result + (transmitterType == null ? 0 : transmitterType.hashCode());
        result = prime * result + (vendor == null ? 0 : vendor.hashCode());
        result = prime * result + Arrays.hashCode(volumeTrim);
        return result;
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

    public void setDscOutputType(final DSCOutputType dscOutput) {
        dscOutputType = dscOutput;
    }

    public void setDualMixer(final DualMixer[] dualMixer) {
        this.dualMixer = dualMixer;
    }

    public void setEscVoiceAnnounceFlag(final long escVoiceAnnounceFlag) {
        this.escVoiceAnnounceFlag = escVoiceAnnounceFlag;
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

    public void setGlobalTrimStep(final int[] globalTrimStep) {
        this.globalTrimStep = globalTrimStep;
    }

    public void setGlobalTrimValue(final int[] globalTrimValue) {
        this.globalTrimValue = globalTrimValue;
    }

    public void setLapStore(final LapStore lapStore) {
        this.lapStore = lapStore;
    }

    public void setLogicalSwitch(final LogicalSwitch[] logicalSwitches) {
        logicalSwitch = logicalSwitches;
    }

    public void setMemoryVersion(final int memoryVersion) {
        this.memoryVersion = memoryVersion;
    }

    public void setModelInfo(final String modelInfo) {
        this.modelInfo = modelInfo;
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

    public void setMp3Player(final Mp3Player mp3Player) {
        this.mp3Player = mp3Player;
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

    public void setReceiver(final Receiver[] receivers) {
        receiver = receivers;
    }

    public void setReceiverBindType(final ReceiverBindType receiverBindType) {
        this.receiverBindType = receiverBindType;
    }

    public void setReceiverType(final ReceiverType reveiverType) {
        receiverType = reveiverType;
    }

    public void setRingLimiter(final RingLimiter[] ringLimiter) {
        this.ringLimiter = ringLimiter;
    }

    public void setSpektrumChannelNumber(final int spektrumChannelNumber) {
        this.spektrumChannelNumber = spektrumChannelNumber;
    }

    public void setSpektrumMode(final SpektrumMode spektrumMode) {
        this.spektrumMode = spektrumMode;
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

    public void setSwitchAnnouncements(final SwitchAnnouncement[] switchAnnouncements) {
        this.switchAnnouncements = switchAnnouncements;
    }

    public void setTelemetry(final Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void setThrottleSettings(final ThrottleSettings throttleSettings) {
        this.throttleSettings = throttleSettings;
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

    public void setVolumeTrim(final int[] volumeTrim) {
        this.volumeTrim = volumeTrim;
    }
}
