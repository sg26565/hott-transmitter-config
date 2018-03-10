/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package de.treichels.hott.model

import de.treichels.hott.messages.Messages
import de.treichels.hott.model.enums.*
import de.treichels.hott.model.enums.Function
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.io.IOException
import java.io.Serializable
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlID
import javax.xml.bind.annotation.XmlIDREF

abstract class AbstractBase : Serializable {
    private val support = PropertyChangeSupport(this)

    val propertyChangeListeners: Array<PropertyChangeListener>
        get() = support.propertyChangeListeners

    fun addPropertyChangeListener(listener: PropertyChangeListener) {
        support.addPropertyChangeListener(listener)
    }

    fun addPropertyChangeListener(propertyName: String, listener: PropertyChangeListener) {
        support.addPropertyChangeListener(propertyName, listener)
    }

    fun fireIndexedPropertyChange(propertyName: String, index: Int, oldValue: Boolean, newValue: Boolean) {
        support.fireIndexedPropertyChange(propertyName, index, oldValue, newValue)
    }

    fun fireIndexedPropertyChange(propertyName: String, index: Int, oldValue: Int, newValue: Int) {
        support.fireIndexedPropertyChange(propertyName, index, oldValue, newValue)
    }

    fun fireIndexedPropertyChange(propertyName: String, index: Int, oldValue: Any, newValue: Any) {
        support.fireIndexedPropertyChange(propertyName, index, oldValue, newValue)
    }

    fun firePropertyChange(event: PropertyChangeEvent) {
        support.firePropertyChange(event)
    }

    fun firePropertyChange(propertyName: String, oldValue: Boolean, newValue: Boolean) {
        support.firePropertyChange(propertyName, oldValue, newValue)
    }

    fun firePropertyChange(propertyName: String, oldValue: Int, newValue: Int) {
        support.firePropertyChange(propertyName, oldValue, newValue)
    }

    fun firePropertyChange(propertyName: String, oldValue: Any, newValue: Any) {
        support.firePropertyChange(propertyName, oldValue, newValue)
    }

    fun getPropertyChangeListeners(propertyName: String): Array<PropertyChangeListener> {
        return support.getPropertyChangeListeners(propertyName)
    }

    fun hasListeners(propertyName: String): Boolean {
        return support.hasListeners(propertyName)
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener) {
        support.removePropertyChangeListener(listener)
    }

    fun removePropertyChangeListener(propertyName: String, listener: PropertyChangeListener) {
        support.removePropertyChangeListener(propertyName, listener)
    }
}

open class HoTTException @JvmOverloads constructor(format: String? = null, cause: Throwable? = null, val args: Array<out Any?>? = null) : IOException(format, cause) {
    constructor(cause: Throwable) : this(format = null, cause = cause, args = null)
    constructor(message: String, vararg args: Any?) : this(format = message, cause = null, args = args)

    override val message: String?
        get() = Messages.getString(super.message, *args ?: emptyArray())
}

abstract class BaseModel(val modelType: ModelType) : AbstractBase() {
    var appVersion: Int = 0
    var isAutoTimerReset: Boolean = false
    @get:XmlIDREF
    var autoTrimSwitch: Switch? = null
    var isBound: Boolean = false
    @get:XmlElementWrapper(name = "channels")
    var channel: List<Channel>? = null
    @get:XmlElementWrapper(name = "channelMappings")
    var channelMapping: List<ChannelMapping>? = null
    var channelSequencer: ChannelSequencer? = null
    @get:XmlElementWrapper(name = "clocks")
    var clock: List<Clock>? = null
    @get:XmlElementWrapper(name = "controlSwitches")
    @get:XmlIDREF
    var controlSwitch: List<ControlSwitch>? = null
    var dscOutputType: DSCOutputType? = null
    @get:XmlElementWrapper(name = "dualMixers")
    var dualMixer: List<DualMixer>? = null
    var extPpmType: ExtPPMType? = null
    var failSafeDelay: Double = 0.0
    var isFailSafeSettingCheck: Boolean = false
    @get:XmlElementWrapper(name = "freeMixers")
    var freeMixer: List<FreeMixer>? = null
    @get:XmlElementWrapper(name = "logicalSwitches")
    @get:XmlIDREF
    var logicalSwitch: List<LogicalSwitch>? = null
    var memoryVersion: Int = 0
    var modelInfo: String = ""
    var modelName: String = ""
    var modelNumber: Int = 0
    var module: HFModule? = null
    var mp3Player: Mp3Player? = null
    @get:XmlElementWrapper(name = "multichannels")
    var multichannel: List<Multichannel>? = null
    @get:XmlElementWrapper(name = "phases")
    var phase: List<Phase>? = null
    var phaseAssignment: PhaseAssignment? = null
    @get:XmlElementWrapper(name = "receivers")
    var receiver: List<Receiver>? = null
    var receiverClass: ReceiverClass? = null
    @get:XmlElementWrapper(name = "ringLimiters")
    var ringLimiter: List<RingLimiter>? = null
    var stickMode: StickMode? = null
    @get:XmlElementWrapper(name = "sicktrims")
    var stickTrim: List<StickTrim>? = null
    @get:XmlElementWrapper(name = "switches")
    var switch: List<Switch>? = null
    var telemetry: Telemetry? = null
    var throttleSettings: ThrottleSettings? = null
    var trainerConfig: TrainerConfig? = null
    var transmitterId: Long = 0L
    var transmitterType: TransmitterType? = null
    var vendor: Vendor? = null
    var spektrumChannelNumber: Int = 0
    var spektrumMode: SpektrumMode? = null
    var volumeTrim: List<Int>? = null
    var globalTrimValue: List<Int>? = null
    var globalTrimStep: List<Int>? = null
    var lapStore: LapStore? = null
    var receiverBindType: ReceiverBindType? = null
    var switchAnnouncements: List<SwitchAnnouncement>? = null
    var escVoiceAnnounceFlag: Long = 0

    fun getSwitch(id: String): Switch? {
        return switch?.firstOrNull { it.id == id }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseModel) return false

        if (modelType != other.modelType) return false
        if (appVersion != other.appVersion) return false
        if (isAutoTimerReset != other.isAutoTimerReset) return false
        if (autoTrimSwitch != other.autoTrimSwitch) return false
        if (isBound != other.isBound) return false
        if (channel != other.channel) return false
        if (channelMapping != other.channelMapping) return false
        if (channelSequencer != other.channelSequencer) return false
        if (clock != other.clock) return false
        if (controlSwitch != other.controlSwitch) return false
        if (dscOutputType != other.dscOutputType) return false
        if (dualMixer != other.dualMixer) return false
        if (extPpmType != other.extPpmType) return false
        if (failSafeDelay != other.failSafeDelay) return false
        if (isFailSafeSettingCheck != other.isFailSafeSettingCheck) return false
        if (freeMixer != other.freeMixer) return false
        if (logicalSwitch != other.logicalSwitch) return false
        if (memoryVersion != other.memoryVersion) return false
        if (modelInfo != other.modelInfo) return false
        if (modelName != other.modelName) return false
        if (modelNumber != other.modelNumber) return false
        if (module != other.module) return false
        if (mp3Player != other.mp3Player) return false
        if (multichannel != other.multichannel) return false
        if (phase != other.phase) return false
        if (phaseAssignment != other.phaseAssignment) return false
        if (receiver != other.receiver) return false
        if (receiverClass != other.receiverClass) return false
        if (ringLimiter != other.ringLimiter) return false
        if (stickMode != other.stickMode) return false
        if (stickTrim != other.stickTrim) return false
        if (switch != other.switch) return false
        if (telemetry != other.telemetry) return false
        if (throttleSettings != other.throttleSettings) return false
        if (trainerConfig != other.trainerConfig) return false
        if (transmitterId != other.transmitterId) return false
        if (transmitterType != other.transmitterType) return false
        if (vendor != other.vendor) return false
        if (spektrumChannelNumber != other.spektrumChannelNumber) return false
        if (spektrumMode != other.spektrumMode) return false
        if (volumeTrim != other.volumeTrim) return false
        if (globalTrimValue != other.globalTrimValue) return false
        if (globalTrimStep != other.globalTrimStep) return false
        if (lapStore != other.lapStore) return false
        if (receiverBindType != other.receiverBindType) return false
        if (switchAnnouncements != other.switchAnnouncements) return false
        if (escVoiceAnnounceFlag != other.escVoiceAnnounceFlag) return false

        return true
    }

    override fun hashCode(): Int {
        var result = modelType.hashCode()
        result = 31 * result + appVersion
        result = 31 * result + isAutoTimerReset.hashCode()
        result = 31 * result + (autoTrimSwitch?.hashCode() ?: 0)
        result = 31 * result + isBound.hashCode()
        result = 31 * result + (channel?.hashCode() ?: 0)
        result = 31 * result + (channelMapping?.hashCode() ?: 0)
        result = 31 * result + (channelSequencer?.hashCode() ?: 0)
        result = 31 * result + (clock?.hashCode() ?: 0)
        result = 31 * result + (controlSwitch?.hashCode() ?: 0)
        result = 31 * result + (dscOutputType?.hashCode() ?: 0)
        result = 31 * result + (dualMixer?.hashCode() ?: 0)
        result = 31 * result + (extPpmType?.hashCode() ?: 0)
        result = 31 * result + failSafeDelay.hashCode()
        result = 31 * result + isFailSafeSettingCheck.hashCode()
        result = 31 * result + (freeMixer?.hashCode() ?: 0)
        result = 31 * result + (logicalSwitch?.hashCode() ?: 0)
        result = 31 * result + memoryVersion
        result = 31 * result + modelInfo.hashCode()
        result = 31 * result + modelName.hashCode()
        result = 31 * result + modelNumber
        result = 31 * result + (module?.hashCode() ?: 0)
        result = 31 * result + (mp3Player?.hashCode() ?: 0)
        result = 31 * result + (multichannel?.hashCode() ?: 0)
        result = 31 * result + (phase?.hashCode() ?: 0)
        result = 31 * result + (phaseAssignment?.hashCode() ?: 0)
        result = 31 * result + (receiver?.hashCode() ?: 0)
        result = 31 * result + (receiverClass?.hashCode() ?: 0)
        result = 31 * result + (ringLimiter?.hashCode() ?: 0)
        result = 31 * result + (stickMode?.hashCode() ?: 0)
        result = 31 * result + (stickTrim?.hashCode() ?: 0)
        result = 31 * result + (switch?.hashCode() ?: 0)
        result = 31 * result + (telemetry?.hashCode() ?: 0)
        result = 31 * result + (throttleSettings?.hashCode() ?: 0)
        result = 31 * result + (trainerConfig?.hashCode() ?: 0)
        result = 31 * result + transmitterId.hashCode()
        result = 31 * result + (transmitterType?.hashCode() ?: 0)
        result = 31 * result + (vendor?.hashCode() ?: 0)
        result = 31 * result + spektrumChannelNumber
        result = 31 * result + (spektrumMode?.hashCode() ?: 0)
        result = 31 * result + (volumeTrim?.hashCode() ?: 0)
        result = 31 * result + (globalTrimValue?.hashCode() ?: 0)
        result = 31 * result + (globalTrimStep?.hashCode() ?: 0)
        result = 31 * result + (lapStore?.hashCode() ?: 0)
        result = 31 * result + (receiverBindType?.hashCode() ?: 0)
        result = 31 * result + (switchAnnouncements?.hashCode() ?: 0)
        result = 31 * result + escVoiceAnnounceFlag.hashCode()
        return result
    }
}

data class Channel(
        var center: Int = 0,
        var failSafeMode: FailSafeMode = FailSafeMode.Undefined,
        var failSafePosition: Double = 0.0,
        var function: Function? = null,
        var limitHigh: Int = 150,
        var limitLow: Int = 150,
        var isMixOnly: Boolean = false,
        var number: Int = 0,
        @get:XmlElementWrapper(name = "phaseSettings")
        var phaseSetting: List<ChannelPhaseSetting> = emptyList(),
        var isReverse: Boolean = false,
        var trainerMode: TrainerMode = TrainerMode.Trainer,
        var travelHigh: Int = 100,
        var travelLow: Int = 100,
        var isVirtual: Boolean = false
) : AbstractBase() {
    @get:XmlAttribute
    @get:XmlID
    val id: String
        get() = number.toString()
}

data class ChannelMapping(
        @get:XmlAttribute
        var inputChannel: Int,
        @get:XmlAttribute
        var outputChannel: Int
) : AbstractBase()

data class ChannelPhaseSetting(
        @get:XmlAttribute
        var isNonDelayed: Boolean,
        @get:XmlIDREF
        @get:XmlAttribute
        var phase: Phase
) : AbstractBase()

data class ChannelSequencer(
        var maxStep: Int,
        var powerOffStatus: SequenceStatus,
        @get:XmlElementWrapper(name = "sequences")
        var sequence: List<Sequence>,
        @get:XmlElementWrapper(name = "stepTimes")
        var stepTime: List<Double>,
        @get:XmlIDREF
        var switch: Switch
) : AbstractBase()

data class Clock(
        var alarm: Int = 0,
        var function: ClockFunction? = null,
        var mode: ClockMode? = null,
        var number: Int,
        @get:XmlIDREF
        var switch: Switch? = null,
        var timer: Int = 0,
        var type: ClockType,
        var value: Int = 0
) : AbstractBase() {
    @get:XmlAttribute
    @get:XmlID
    val id: String
        get() = number.toString()

    var minutes: Int
        get() = value / 60
        set(minutes) {
            value = minutes * 60 + seconds
        }

    var seconds: Int
        get() = value % 60
        set(seconds) {
            value = minutes * 60 + seconds
        }

    var timerMinutes: Int
        get() = timer / 60
        set(minutes) {
            timer = minutes * 60 + timerSeconds
        }

    var timerSeconds: Int
        get() = timer % 60
        set(seconds) {
            timer = timerMinutes * 60 + seconds
        }
}

data class Control(
        @get:XmlIDREF
        var inputControl: Switch? = null,
        var mode: ControlMode? = null,
        var number: Int = 0,
        var offset: Int = 0,
        var timeHigh: Double = 0.0,
        var timeLow: Double = 0.0,
        @get:XmlIDREF
        var toggleHighSwitch: Switch? = null,
        @get:XmlIDREF
        var toggleLowSwitch: Switch? = null,
        var travelHigh: Int = 0,
        var travelLow: Int = 0,
        var trim: Int = 0
) : AbstractBase() {
    @get:XmlAttribute
    @get:XmlID
    val id: String
        get() = number.toString()
}

class ControlSwitch(
        @get:XmlIDREF
        var combineSwitch: Switch? = null,
        var isEnabled: Boolean = false,
        var position: Int = 0
) : Switch() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ControlSwitch) return false
        if (!super.equals(other)) return false

        if (combineSwitch != other.combineSwitch) return false
        if (isEnabled != other.isEnabled) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (combineSwitch?.hashCode() ?: 0)
        result = 31 * result + isEnabled.hashCode()
        result = 31 * result + position
        return result
    }
}

data class Curve(
        @get:XmlElementWrapper(name = "points")
        var point: List<CurvePoint>? = null,
        var isSmoothing: Boolean = false,
        var type: CurveType = CurveType.Unused
) : AbstractBase()

open class CurveMixer(type: MixerType = MixerType.Curve) : FreeMixer(type) {
    var curve: Curve? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CurveMixer) return false
        if (!super.equals(other)) return false

        if (curve != other.curve) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (curve?.hashCode() ?: 0)
        return result
    }
}

data class CurvePoint(
        var isEnabled: Boolean = false,
        @get:XmlAttribute
        var number: Int = 0,
        var position: Int = 0,
        var value: Int = 0
) : AbstractBase()

data class DualMixer(
        @get:XmlIDREF
        var channel: List<Channel>? = null,
        var diff: Int = 0,
        var number: Int = 0,
        val type: MixerType = MixerType.Dual
) : AbstractBase()

data class DualRate(
        @get:XmlAttribute
        var function: Function? = null,
        @get:XmlIDREF
        var switch: Switch? = null,
        var values: List<Int>? = null
) : AbstractBase()

data class DualRateExpo(
        var dualRate: DualRate? = null,
        var expo: Expo? = null
) : AbstractBase() {
    val curve: List<Curve>?
        get() {
            val ex = expo?.values
            val dr = dualRate?.values

            return if (ex != null && dr != null)
                listOf(
                        getCurve(dr[0], ex[0]),
                        getCurve(dr[0], ex[1]),
                        getCurve(dr[1], ex[0]),
                        getCurve(dr[1], ex[1])
                )
            else
                null
        }

    private fun getCurve(dr: Int, expo: Int): Curve {
        return Curve(point = listOf(
                CurvePoint(number = 0, isEnabled = true, position = -100, value = -dr),
                CurvePoint(number = 0, isEnabled = true, position = -50, value = (-50 + expo) * dr / 100),
                CurvePoint(number = 0, isEnabled = true, position = 0, value = 0),
                CurvePoint(number = 0, isEnabled = true, position = 50, value = (50 - expo) * dr / 100),
                CurvePoint(number = 0, isEnabled = true, position = 100, value = dr)
        ), isSmoothing = true)
    }
}

data class Expo(
        @get:XmlAttribute
        var function: Function,
        @get:XmlIDREF
        var switch: Switch? = null,
        var values: List<Int>? = null
) : AbstractBase()

open class FreeMixer(val type: MixerType) : AbstractBase() {
    @get:XmlIDREF
    var fromChannel: Channel? = null
    var inputType: MixerInputType? = null
    var number: Int = 0
    @get:XmlAttribute
    @get:XmlID
    val id: String
        get() = number.toString()
    @get:XmlElementWrapper(name = "phaseSettings")
    var phaseSetting: List<FreeMixerPhaseSetting>? = null
    @get:XmlIDREF
    var switch: Switch? = null
    @get:XmlIDREF
    var toChannel: Channel? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FreeMixer) return false

        if (type != other.type) return false
        if (fromChannel != other.fromChannel) return false
        if (inputType != other.inputType) return false
        if (number != other.number) return false
        if (id != other.id) return false
        if (phaseSetting != other.phaseSetting) return false
        if (switch != other.switch) return false
        if (toChannel != other.toChannel) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + (fromChannel?.hashCode() ?: 0)
        result = 31 * result + (inputType?.hashCode() ?: 0)
        result = 31 * result + number
        result = 31 * result + id.hashCode()
        result = 31 * result + (phaseSetting?.hashCode() ?: 0)
        result = 31 * result + (switch?.hashCode() ?: 0)
        result = 31 * result + (toChannel?.hashCode() ?: 0)
        return result
    }
}

data class FreeMixerPhaseSetting(
        @get:XmlAttribute
        var isEnabled: Boolean,
        @get:XmlIDREF
        @get:XmlAttribute
        var phase: Phase
) : AbstractBase()

data class HFModule(
        var type: HFModuleType
) : AbstractBase()

data class Lap(
        var minute: Int,
        var second: Int,
        var millisecond: Int
) : AbstractBase()

data class LapStore(
        var isActive: Boolean,
        var viewLap: Int,
        var currentLap: Int,
        var laps: List<Lap>
) : AbstractBase()

class LinearMixer : CurveMixer(type = MixerType.Linear) {
    var offset: Int = 0
        set(offset) {
            field = offset
            updateCurve()
        }
    var travelHigh: Int = 100
        set(travelHigh) {
            field = travelHigh
            updateCurve()
        }
    var travelLow: Int = -100
        set(travelLow) {
            field = travelLow
            updateCurve()
        }

    private fun updateCurve() {
        this.curve = Curve(point = listOf(
                CurvePoint(number = 1, isEnabled = true, position = -100, value = (-travelLow * (1.0f + offset / 100.0f)).toInt()),
                CurvePoint(number = 1, isEnabled = true, position = offset, value = 0),
                CurvePoint(number = 1, isEnabled = true, position = 100, value = (travelHigh * (1.0f - offset / 100.0f)).toInt())
        ))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinearMixer) return false
        if (!super.equals(other)) return false

        if (offset != other.offset) return false
        if (travelHigh != other.travelHigh) return false
        if (travelLow != other.travelLow) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + offset
        result = 31 * result + travelHigh
        result = 31 * result + travelLow
        return result
    }
}

class LogicalSwitch(
        var isEnabled: Boolean = false,
        var mode: LogicalSwitchMode? = null,
        @get:XmlElementWrapper(name = "switches")
        @get:XmlIDREF
        var switch: List<Switch>? = null
) : Switch() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LogicalSwitch) return false
        if (!super.equals(other)) return false

        if (isEnabled != other.isEnabled) return false
        if (mode != other.mode) return false
        if (switch != other.switch) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + isEnabled.hashCode()
        result = 31 * result + (mode?.hashCode() ?: 0)
        result = 31 * result + (switch?.hashCode() ?: 0)
        return result
    }
}

data class Mp3Player(
        var album: Int,
        var mode: Mp3PlayerMode,
        @get:XmlIDREF
        var playPauseSwitch: Switch? = null,
        var track: Int,
        var volume: Int,
        @get:XmlIDREF
        var volumeLeftSwitch: Switch? = null,
        @get:XmlIDREF
        var volumeRightSwitch: Switch? = null,
        @get:XmlIDREF
        var volumeSwitch: Switch? = null
) : AbstractBase()

data class Multichannel(
        @get:XmlElementWrapper(name = "controls")
        var control: List<Control>,
        var isEnabled: Boolean,
        @get:XmlIDREF
        var inputChannel: Channel? = null,
        var mode: MultichannelMode? = null,
        @get:XmlAttribute
        var number: Int
) : AbstractBase()

abstract class Phase : AbstractBase() {
    var channel1Curve: Curve? = null
    @get:XmlElementWrapper(name = "controls")
    var control: List<Control>? = null
    @get:XmlElementWrapper(name = "dualRateExpos")
    var dualRateExpo: List<DualRateExpo>? = null
    var isMotorOn: Boolean = false
    var number: Int = 0
    @get:XmlAttribute
    @get:XmlID
    val id: String
        get() = number.toString()
    var phaseName: String? = null
    @get:XmlIDREF
    var phaseSwitch: Switch? = null
    var phaseSwitchTime: Double = 0.0
    @get:XmlIDREF
    var phaseTimer: Clock? = null
    var phaseType: PhaseType? = null
    var digitalTrimValue: List<Int>? = null
    var digitalTrimStep: List<Int>? = null
    var switchAnnouncements: List<SwitchAnnouncement>? = null
    var phaseAnnouncement: String? = null

    override fun toString(): String {
        return String.format("Phase %d: %s", number + 1, phaseName)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Phase) return false

        if (channel1Curve != other.channel1Curve) return false
        if (control != other.control) return false
        if (dualRateExpo != other.dualRateExpo) return false
        if (isMotorOn != other.isMotorOn) return false
        if (number != other.number) return false
        if (phaseName != other.phaseName) return false
        if (phaseSwitch != other.phaseSwitch) return false
        if (phaseSwitchTime != other.phaseSwitchTime) return false
        if (phaseTimer != other.phaseTimer) return false
        if (phaseType != other.phaseType) return false
        if (digitalTrimValue != other.digitalTrimValue) return false
        if (digitalTrimStep != other.digitalTrimStep) return false
        if (switchAnnouncements != other.switchAnnouncements) return false
        if (phaseAnnouncement != other.phaseAnnouncement) return false

        return true
    }

    override fun hashCode(): Int {
        var result = channel1Curve?.hashCode() ?: 0
        result = 31 * result + (control?.hashCode() ?: 0)
        result = 31 * result + (dualRateExpo?.hashCode() ?: 0)
        result = 31 * result + isMotorOn.hashCode()
        result = 31 * result + (number.hashCode())
        result = 31 * result + (phaseName?.hashCode() ?: 0)
        result = 31 * result + (phaseSwitch?.hashCode() ?: 0)
        result = 31 * result + phaseSwitchTime.hashCode()
        result = 31 * result + (phaseTimer?.hashCode() ?: 0)
        result = 31 * result + (phaseType?.hashCode() ?: 0)
        result = 31 * result + (digitalTrimValue?.hashCode() ?: 0)
        result = 31 * result + (digitalTrimStep?.hashCode() ?: 0)
        result = 31 * result + (switchAnnouncements?.hashCode() ?: 0)
        result = 31 * result + (phaseAnnouncement?.hashCode() ?: 0)
        return result
    }
}

data class PhaseAssignment(
        @get:XmlIDREF
        @get:XmlElementWrapper(name = "assignments")
        var assignment: List<Phase>,
        @get:XmlIDREF
        var combiCSwitch: Switch,
        @get:XmlIDREF
        var combiDSwitch: Switch,
        @get:XmlIDREF
        var combiESwitch: Switch,
        @get:XmlIDREF
        var combiFSwitch: Switch,
        @get:XmlIDREF
        var priorityASwitch: Switch,
        @get:XmlIDREF
        var priorityBSwitch: Switch
) : AbstractBase() {
    val normalPhase: Phase?
        get() = assignment[0]

    val priorityAPhase: Phase?
        get() = assignment[1]

    val priorityBPhase: Phase?
        get() = assignment[2]

    fun getCombiPhase(c: Boolean, d: Boolean, e: Boolean, f: Boolean): Phase? {
        val number = (if (c) 8 else 0) + (if (d) 4 else 0) + (if (e) 2 else 0) + if (f) 1 else 0

        return getCombiPhase(number)
    }

    private fun getCombiPhase(number: Int): Phase? {
        return if (number == 0)
            normalPhase
        else
            assignment[number + 2]
    }
}

data class Receiver(
        var isBound: Boolean = false,
        @get:XmlElementWrapper(name = "channelMappings")
        var channelMapping: List<ChannelMapping>? = null,
        var number: Int = 0,
        var rfid: Long = 0L,
        var isTelemetry: Boolean = false,
        var firmwareType: ReceiverFirmwareType? = null
) : AbstractBase() {
    @get:XmlAttribute
    @get:XmlID
    val id: String
        get() = number.toString()
}

data class RingLimiter(
        var isEnabled: Boolean,
        @get:XmlIDREF
        @get:XmlElementWrapper(name = "inputChannels")
        var inputChannel: List<Channel>,
        @get:XmlElementWrapper(name = "limits")
        var limit: List<Int>,
        @get:XmlAttribute
        var number: Int,
        @get:XmlElementWrapper(name = "offsets")
        var offset: List<Int>,
        @get:XmlIDREF
        @get:XmlElementWrapper(name = "outputChannels")
        var outputChannel: List<Channel>
) : AbstractBase()

data class Sequence(
        var isEnabled: Boolean = false,
        @get:XmlAttribute
        var number: Int = 0,
        @get:XmlIDREF
        var outputChannel: Channel? = null,
        var stepPosition: List<Int>? = null
) : AbstractBase()

data class StickTrim(
        @get:XmlAttribute
        var channel: Int = 0,
        var increment: Int = 0,
        var mode: TrimMode,
        var timeHigh: Int = 0,
        var timeLow: Int = 0
) : AbstractBase()

open class Switch(
        var assignment: SwitchAssignment = SwitchAssignment.Unassigned,
        var direction: Int = 0,
        var function: SwitchFunction = SwitchFunction.Unassigned,
        var number: Int = 0,
        var qualifier: List<Any>? = null
) : AbstractBase() {
    val id: String
        @XmlID
        @XmlAttribute
        get() {
            val b = StringBuilder()

            b.append(function.name)

            val qual = qualifier
            if (qual != null)
                for (q in qual) {
                    b.append("_")

                    if (q.javaClass.isEnum)
                        try {
                            val m = q.javaClass.getMethod("name")
                            b.append(m.invoke(q))
                        } catch (e: Exception) {
                            throw RuntimeException(e)
                        }
                    else
                        b.append(q.toString())
                }

            return b.toString()
        }

    val name: String
        get() {
            val b = StringBuilder()

            b.append(function.toString())

            val qual = qualifier
            if (qual != null)
                for (q in qual) {
                    b.append(" ")

                    if (q.javaClass.isEnum)
                        try {
                            val m = q.javaClass.getMethod("toString")
                            b.append(m.invoke(q))
                        } catch (e: Exception) {
                            throw RuntimeException(e)
                        }
                    else
                        b.append(q.toString())
                }

            return b.toString()
        }

    val type: SwitchType = assignment.type

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Switch) return false

        if (assignment != other.assignment) return false
        if (direction != other.direction) return false
        if (function != other.function) return false
        if (number != other.number) return false
        if (qualifier != other.qualifier) return false

        return true
    }

    override fun hashCode(): Int {
        var result = assignment.hashCode()
        result = 31 * result + direction
        result = 31 * result + (function.hashCode())
        result = 31 * result + number
        result = 31 * result + (qualifier?.hashCode() ?: 0)
        return result
    }
}

data class SwitchAnnouncement(
        var announcementType: AnnouncementType? = null,
        var switch: Switch? = null,
        var name: List<String>? = null
) : AbstractBase()

data class Telemetry(
        var currentSensor: SensorType = SensorType.None,
        var currentSensorPage: Int = 0,
        var selectedSensor: List<SensorType>? = null,
        var varioTone: Switch? = null,
        var voiceDelay: Int = 0,
        var voiceRepeat: Switch? = null,
        var voiceTrigger: Switch? = null,
        var varioToneSensor: VarioToneSensor? = null,
        var userAlarmList: List<String>? = null,
        var telemetryDataReceiveTime: Int = 0,
        var telemetryAlarmType: TelemetryAlarmType = TelemetryAlarmType.Off,
        var basicVoiceList: Int = 0,
        var generalAirVoiceList: Int = 0,
        var electricAirVoiceList: Int = 0,
        var varioVoiceList: Int = 0,
        var gpsVoiceList: Int = 0
) : AbstractBase()

data class TrainerConfig(
        @get:XmlIDREF
        var pupilChannel: List<Channel>? = null,
        var pupilId: Long = 0L,
        @get:XmlIDREF
        var trainerSwitch: Switch? = null,
        @get:XmlIDREF
        var trainerChannel: List<Channel>? = null,
        var trainerId: Long = 0L,
        var isWireless: Boolean = false
) : AbstractBase()

data class ThrottleCutOf(
        var position: Int,
        @get:XmlIDREF
        var switch: Switch? = null,
        var threshold: Int
) : AbstractBase()

data class ThrottleSettings(
        var throttleCutOf: ThrottleCutOf,
        var throttleLastIdlePosition: Int = 0,
        var throttleTrim: Int = 0
) : AbstractBase()
