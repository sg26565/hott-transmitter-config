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
import javax.xml.bind.annotation.*

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

@XmlAccessorType(XmlAccessType.PROPERTY)
abstract class BaseModel(val modelType: ModelType) : AbstractBase() {
    var appVersion: Int = 0
    var isAutoTimerReset: Boolean = false
    @get:XmlIDREF
    var autoTrimSwitch: Switch = Switch()
    var isBound: Boolean = false
    @get:XmlElementWrapper(name = "channels")
    var channel = emptyList<Channel>()
    @get:XmlElementWrapper(name = "channelMappings")
    var channelMapping = emptyList<ChannelMapping>()
    var channelSequencer: ChannelSequencer? = null
    @get:XmlElementWrapper(name = "clocks")
    var clock = emptyList<Clock>()
    @get:XmlElementWrapper(name = "controlSwitches")
    @get:XmlIDREF
    var controlSwitch = emptyList<ControlSwitch>()
    var dscOutputType: DSCOutputType = DSCOutputType.Unknown
    @get:XmlElementWrapper(name = "dualMixers")
    var dualMixer = emptyList<DualMixer>()
    var extPpmType = ExtPPMType.Unknown
    var failSafeDelay: Double = 0.0
    var isFailSafeSettingCheck: Boolean = false
    @get:XmlElementWrapper(name = "freeMixers")
    var freeMixer = emptyList<FreeMixer>()
    @get:XmlElementWrapper(name = "logicalSwitches")
    @get:XmlIDREF
    var logicalSwitch = emptyList<LogicalSwitch>()
    var memoryVersion: Int = 0
    var modelInfo: String = ""
    var modelName: String = ""
    var modelNumber: Int = 0
    var module = HFModule()
    var motorOnC1Type: MotorOnC1Type = MotorOnC1Type.Unknown
    var mp3Player = Mp3Player()
    @get:XmlElementWrapper(name = "multichannels")
    var multichannel = emptyList<Multichannel>()
    @get:XmlElementWrapper(name = "phases")
    var phase = emptyList<Phase>()
    var phaseAssignment: PhaseAssignment? = null
    @get:XmlElementWrapper(name = "receivers")
    var receiver = emptyList<Receiver>()
    var receiverClass: ReceiverClass = ReceiverClass.Unknown
    @get:XmlElementWrapper(name = "ringLimiters")
    var ringLimiter = emptyList<RingLimiter>()
    var stickMode: StickMode = StickMode.Undefined
    @get:XmlElementWrapper(name = "sicktrims")
    var stickTrim = emptyList<StickTrim>()
    @get:XmlElementWrapper(name = "switches")
    var switch = emptyList<Switch>()
    var telemetry: Telemetry = Telemetry()
    var throttleSettings = ThrottleSettings()
    var trainerConfig = TrainerConfig()
    var transmitterId: Long = 0L
    var transmitterType = TransmitterType.Unknown
    var vendor: Vendor = Vendor.Unknown
    var spektrumChannelNumber: Int = 0
    var spektrumMode = SpektrumMode.NONE
    var volumeTrim = emptyList<Int>()
    var globalTrimValue = emptyList<Int>()
    var globalTrimStep = emptyList<Int>()
    var lapStore = LapStore()
    var receiverBindType = ReceiverBindType.Unknown
    var switchAnnouncements = emptyList<SwitchAnnouncement>()
    var escVoiceAnnounceFlag: Long = 0
    val suppressMenus: MutableMap<Menus, Boolean> = mutableMapOf()
    var showHiddenMenus = false

    fun getSwitch(id: String): Switch? {
        return switch.firstOrNull { it.id == id }
    }

    fun isMenuEnabled(name: String): Boolean {
        val menus = Menus.values().find { it.name == name }
        return if (menus != null)
            showHiddenMenus || !(suppressMenus[menus] ?: false)
        else
            true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseModel

        if (modelType != other.modelType) return false
        if (appVersion != other.appVersion) return false
        if (isAutoTimerReset != other.isAutoTimerReset) return false
        if (isBound != other.isBound) return false
        if (channelSequencer != other.channelSequencer) return false
        if (dscOutputType != other.dscOutputType) return false
        if (extPpmType != other.extPpmType) return false
        if (failSafeDelay != other.failSafeDelay) return false
        if (isFailSafeSettingCheck != other.isFailSafeSettingCheck) return false
        if (memoryVersion != other.memoryVersion) return false
        if (modelInfo != other.modelInfo) return false
        if (modelName != other.modelName) return false
        if (modelNumber != other.modelNumber) return false
        if (module != other.module) return false
        if (motorOnC1Type != other.motorOnC1Type) return false
        if (mp3Player != other.mp3Player) return false
        if (phaseAssignment != other.phaseAssignment) return false
        if (receiverClass != other.receiverClass) return false
        if (stickMode != other.stickMode) return false
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
        result = 31 * result + isBound.hashCode()
        result = 31 * result + (channelSequencer?.hashCode() ?: 0)
        result = 31 * result + dscOutputType.hashCode()
        result = 31 * result + extPpmType.hashCode()
        result = 31 * result + failSafeDelay.hashCode()
        result = 31 * result + isFailSafeSettingCheck.hashCode()
        result = 31 * result + memoryVersion
        result = 31 * result + modelInfo.hashCode()
        result = 31 * result + modelName.hashCode()
        result = 31 * result + modelNumber
        result = 31 * result + module.hashCode()
        result = 31 * result + motorOnC1Type.hashCode()
        result = 31 * result + mp3Player.hashCode()
        result = 31 * result + (phaseAssignment?.hashCode() ?: 0)
        result = 31 * result + receiverClass.hashCode()
        result = 31 * result + stickMode.hashCode()
        result = 31 * result + telemetry.hashCode()
        result = 31 * result + throttleSettings.hashCode()
        result = 31 * result + trainerConfig.hashCode()
        result = 31 * result + transmitterId.hashCode()
        result = 31 * result + transmitterType.hashCode()
        result = 31 * result + vendor.hashCode()
        result = 31 * result + spektrumChannelNumber
        result = 31 * result + spektrumMode.hashCode()
        result = 31 * result + volumeTrim.hashCode()
        result = 31 * result + globalTrimValue.hashCode()
        result = 31 * result + globalTrimStep.hashCode()
        result = 31 * result + lapStore.hashCode()
        result = 31 * result + receiverBindType.hashCode()
        result = 31 * result + switchAnnouncements.hashCode()
        result = 31 * result + escVoiceAnnounceFlag.hashCode()
        return result
    }

}

@XmlAccessorType(XmlAccessType.PROPERTY)
data class Channel(
        var center: Int = 0,
        var failSafeMode: FailSafeMode = FailSafeMode.Undefined,
        var failSafePosition: Double = 0.0,
        var function: Function = Function.Unknown,
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

@XmlAccessorType(XmlAccessType.PROPERTY)
data class ChannelMapping(
        @get:XmlAttribute
        var inputChannel: Int,
        @get:XmlAttribute
        var outputChannel: Int
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class ChannelPhaseSetting(
        @get:XmlAttribute
        var isNonDelayed: Boolean,
        @get:XmlIDREF
        @get:XmlAttribute
        var phase: Phase
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class ChannelSequencer(
        var maxStep: Int,
        var powerOffStatus: SequenceStatus,
        @get:XmlElementWrapper(name = "sequences")
        var sequence: List<Sequence> = emptyList(),
        @get:XmlElementWrapper(name = "stepTimes")
        var stepTime: List<Double> = emptyList(),
        @get:XmlIDREF
        var switch: Switch
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class Clock(
        var alarm: Int = 0,
        var function: ClockFunction = ClockFunction.Unknown,
        var value: Int = 0,
        var number: Int,
        @get:XmlIDREF
        var switch: Switch = Switch(),
        var timer: Int = 0,
        var type: ClockType
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

    val mode: ClockMode
        get() = if (value == 0) ClockMode.Timer else ClockMode.CountDown
}

@XmlAccessorType(XmlAccessType.PROPERTY)
data class Control(
        @get:XmlIDREF
        var inputControl: Switch = Switch(),
        var mode: ControlMode = ControlMode.Unknown,
        var function: Function = Function.Unknown,
        var number: Int = 0,
        var offset: Int = 0,
        var timeHigh: Double = 0.0,
        var timeLow: Double = 0.0,
        @get:XmlIDREF
        var toggleHighSwitch: Switch = Switch(),
        @get:XmlIDREF
        var toggleLowSwitch: Switch = Switch(),
        var travelHigh: Int = 0,
        var travelLow: Int = 0,
        var trim: Int = 0
) : AbstractBase() {
    @get:XmlAttribute
    @get:XmlID
    val id: String
        get() = number.toString()
}

@XmlAccessorType(XmlAccessType.PROPERTY)
class ControlSwitch(
        @get:XmlIDREF
        var combineSwitch: Switch = Switch(),
        var isEnabled: Boolean = false
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
        result = 31 * result + combineSwitch.hashCode()
        result = 31 * result + isEnabled.hashCode()
        result = 31 * result + position
        return result
    }
}

@XmlAccessorType(XmlAccessType.PROPERTY)
data class Curve(
        @get:XmlElementWrapper(name = "points")
        var point: List<CurvePoint> = emptyList(),
        var isSmoothing: Boolean = false,
        var type: CurveType = CurveType.Unused
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
open class CurveMixer(type: MixerType = MixerType.Curve) : FreeMixer(type) {
    lateinit var curve: Curve

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CurveMixer) return false
        if (!super.equals(other)) return false

        if (curve != other.curve) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + curve.hashCode()
        return result
    }
}

@XmlAccessorType(XmlAccessType.PROPERTY)
data class CurvePoint(
        var isEnabled: Boolean = false,
        @get:XmlAttribute
        var number: Int = 0,
        var position: Int = 0,
        var value: Int = 0
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class DualMixer(
        @get:XmlIDREF
        var channel: List<Channel> = emptyList(),
        var diff: Int = 0,
        var number: Int = 0,
        val type: MixerType = MixerType.Dual
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class DualRate(
        @get:XmlAttribute
        var function: Function,
        @get:XmlIDREF
        var switch: Switch = Switch(),
        var values: List<Int> = emptyList()
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class DualRateExpo(
        var dualRate: DualRate,
        var expo: Expo
) : AbstractBase() {
    val curve: List<Curve>
        get() {
            val ex = expo.values
            val dr = dualRate.values

            return listOf(
                    getCurve(dr[0], ex[0]),
                    getCurve(dr[0], ex[1]),
                    getCurve(dr[1], ex[0]),
                    getCurve(dr[1], ex[1])
            )
        }

    private fun getCurve(dr: Int, expo: Int): Curve {
        val x = 50 + expo * 28 / 100
        val y = dr / 2

        return Curve(point = listOf(
                CurvePoint(number = 0, isEnabled = true, position = -100, value = -dr),
                CurvePoint(number = 1, isEnabled = true, position = -x, value = -y),
                CurvePoint(number = 2, isEnabled = true, position = 0, value = 0),
                CurvePoint(number = 3, isEnabled = true, position = x, value = y),
                CurvePoint(number = 4, isEnabled = true, position = 100, value = dr)
        ), isSmoothing = true)
    }
}

@XmlAccessorType(XmlAccessType.PROPERTY)
data class Expo(
        @get:XmlAttribute
        var function: Function,
        @get:XmlIDREF
        var switch: Switch = Switch(),
        var values: List<Int> = emptyList()
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
open class FreeMixer(val type: MixerType) : AbstractBase() {
    @get:XmlIDREF
    lateinit var fromChannel: Channel
    var inputType: MixerInputType = MixerInputType.Unknown
    var number: Int = 0
    @get:XmlAttribute
    @get:XmlID
    val id: String
        get() = number.toString()
    @get:XmlElementWrapper(name = "phaseSettings")
    var phaseSetting = emptyList<FreeMixerPhaseSetting>()
    @get:XmlIDREF
    var switch: Switch = Switch()
    @get:XmlIDREF
    lateinit var toChannel: Channel

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
        result = 31 * result + fromChannel.hashCode()
        result = 31 * result + inputType.hashCode()
        result = 31 * result + number
        result = 31 * result + id.hashCode()
        result = 31 * result + phaseSetting.hashCode()
        result = 31 * result + switch.hashCode()
        result = 31 * result + toChannel.hashCode()
        return result
    }
}

@XmlAccessorType(XmlAccessType.PROPERTY)
data class FreeMixerPhaseSetting(
        @get:XmlAttribute
        var isEnabled: Boolean,
        @get:XmlIDREF
        @get:XmlAttribute
        var phase: Phase
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class HFModule(
        var type: HFModuleType = HFModuleType.Unknown
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class Lap(
        var minute: Int,
        var second: Int,
        var millisecond: Int
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class LapStore(
        var isActive: Boolean = false,
        var viewLap: Int = 0,
        var currentLap: Int = 0,
        var laps: List<Lap> = emptyList()
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
class LinearMixer : CurveMixer(type = MixerType.Linear) {
    var xOffset: Int = 0
        set(offset) {
            field = offset
            updateCurve()
        }
    var yOffset: Int = 0
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
                CurvePoint(number = 1, isEnabled = true, position = -100, value = (-travelLow * (1.0f + xOffset / 100.0f)).toInt()),
                CurvePoint(number = 2, isEnabled = true, position = xOffset, value = yOffset),
                CurvePoint(number = 3, isEnabled = true, position = 100, value = (travelHigh * (1.0f - xOffset / 100.0f)).toInt())
        ))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinearMixer) return false
        if (!super.equals(other)) return false

        if (xOffset != other.xOffset) return false
        if (yOffset != other.yOffset) return false
        if (travelHigh != other.travelHigh) return false
        if (travelLow != other.travelLow) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + xOffset
        result = 31 * result + yOffset
        result = 31 * result + travelHigh
        result = 31 * result + travelLow
        return result
    }
}

@XmlAccessorType(XmlAccessType.PROPERTY)
class LogicalSwitch(
        var isEnabled: Boolean = false,
        var mode: LogicalSwitchMode = LogicalSwitchMode.Unknown,
        @get:XmlElementWrapper(name = "switches")
        @get:XmlIDREF
        var switch: List<Switch> = emptyList()
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
        result = 31 * result + mode.hashCode()
        result = 31 * result + switch.hashCode()
        return result
    }
}

@XmlAccessorType(XmlAccessType.PROPERTY)
data class Mp3Player(
        var album: Int = 0,
        var mode: Mp3PlayerMode = Mp3PlayerMode.Unknown,
        @get:XmlIDREF
        var playPauseSwitch: Switch = Switch(),
        var track: Int = 0,
        var volume: Int = 0,
        @get:XmlIDREF
        var volumeLeftSwitch: Switch = Switch(),
        @get:XmlIDREF
        var volumeRightSwitch: Switch = Switch(),
        @get:XmlIDREF
        var volumeSwitch: Switch = Switch()
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class Multichannel(
        @get:XmlElementWrapper(name = "controls")
        var control: List<Control> = emptyList(),
        var isEnabled: Boolean,
        @get:XmlIDREF
        var inputChannel: Channel = Channel(),
        var mode: MultichannelMode = MultichannelMode.Unknown,
        @get:XmlAttribute
        var number: Int
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
abstract class Phase : AbstractBase() {
    var channel1Curve: Curve = Curve()
    @get:XmlElementWrapper(name = "controls")
    var control = emptyList<Control>()
    @get:XmlElementWrapper(name = "dualRateExpos")
    var dualRateExpo = emptyList<DualRateExpo>()
    var isMotorOn: Boolean = false
    var number: Int = 0
    @get:XmlAttribute
    @get:XmlID
    val id: String
        get() = number.toString()
    var phaseName: String = ""
    @get:XmlIDREF
    var phaseSwitch: Switch = Switch()
    var phaseSwitchTime: Double = 0.0
    @get:XmlIDREF
    var phaseTimer: Clock? = null
    var phaseType: PhaseType = PhaseType.Unnamed
    var digitalTrimValue = emptyList<Int>()
    var digitalTrimStep = emptyList<Int>()
    var switchAnnouncements = emptyList<SwitchAnnouncement>()
    var phaseAnnouncement: String = ""

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
        var result = channel1Curve.hashCode()
        result = 31 * result + control.hashCode()
        result = 31 * result + dualRateExpo.hashCode()
        result = 31 * result + isMotorOn.hashCode()
        result = 31 * result + (number.hashCode())
        result = 31 * result + phaseName.hashCode()
        result = 31 * result + phaseSwitch.hashCode()
        result = 31 * result + phaseSwitchTime.hashCode()
        result = 31 * result + phaseTimer.hashCode()
        result = 31 * result + phaseType.hashCode()
        result = 31 * result + digitalTrimValue.hashCode()
        result = 31 * result + digitalTrimStep.hashCode()
        result = 31 * result + switchAnnouncements.hashCode()
        result = 31 * result + phaseAnnouncement.hashCode()
        return result
    }
}

@XmlAccessorType(XmlAccessType.PROPERTY)
data class PhaseAssignment(
        @get:XmlIDREF
        @get:XmlElementWrapper(name = "assignments")
        var assignment: List<Phase> = emptyList(),
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
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class Receiver(
        var isBound: Boolean = false,
        @get:XmlElementWrapper(name = "channelMappings")
        var channelMapping: List<ChannelMapping> = emptyList(),
        var number: Int = 0,
        var rfid: Long = 0L,
        var isTelemetry: Boolean = false,
        var firmwareType: ReceiverFirmwareType = ReceiverFirmwareType.Unknown
) : AbstractBase() {
    @get:XmlAttribute
    @get:XmlID
    val id: String
        get() = number.toString()
}

@XmlAccessorType(XmlAccessType.PROPERTY)
data class RingLimiter(
        var isEnabled: Boolean,
        @get:XmlIDREF
        @get:XmlElementWrapper(name = "inputChannels")
        var inputChannel: List<Channel> = emptyList(),
        @get:XmlElementWrapper(name = "limits")
        var limit: List<Int> = emptyList(),
        @get:XmlAttribute
        var number: Int,
        @get:XmlElementWrapper(name = "offsets")
        var offset: List<Int> = emptyList(),
        @get:XmlIDREF
        @get:XmlElementWrapper(name = "outputChannels")
        var outputChannel: List<Channel> = emptyList()
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class Sequence(
        var isEnabled: Boolean = false,
        @get:XmlAttribute
        var number: Int = 0,
        @get:XmlIDREF
        var outputChannel: Channel = Channel(),
        var stepPosition: List<Int> = emptyList()
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class StickTrim(
        @get:XmlAttribute
        var channel: Int = 0,
        var increment: Int = 0,
        var mode: TrimMode,
        var timeHigh: Double = 0.0,
        var timeLow: Double = 0.0
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
open class Switch(
        var assignment: SwitchAssignment = SwitchAssignment.Unassigned,
        var reverse: Boolean = false,
        var position: Int =0,
        var switchMode: SwitchMode = SwitchMode.Unknown,
        var high: Boolean = false,
        var mid: Boolean = false,
        var low: Boolean = false,
        var function: SwitchFunction = SwitchFunction.Unassigned,
        var number: Int = 0,
        var qualifier: List<Any> = emptyList(),
        var active: SwitchActive = SwitchActive.Unknown
) : AbstractBase() {
    val id: String
        @XmlID
        @XmlAttribute
        get() {
            val b = StringBuilder()

            b.append(function.name)

            val qual = qualifier
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
        if (javaClass != other?.javaClass) return false

        other as Switch

        if (assignment != other.assignment) return false
        if (reverse != other.reverse) return false
        if (position != other.position) return false
        if (switchMode != other.switchMode) return false
        if (high != other.high) return false
        if (mid != other.mid) return false
        if (low != other.low) return false
        if (function != other.function) return false
        if (number != other.number) return false
        if (qualifier != other.qualifier) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = assignment.hashCode()
        result = 31 * result + reverse.hashCode()
        result = 31 * result + position
        result = 31 * result + switchMode.hashCode()
        result = 31 * result + high.hashCode()
        result = 31 * result + mid.hashCode()
        result = 31 * result + low.hashCode()
        result = 31 * result + function.hashCode()
        result = 31 * result + number
        result = 31 * result + qualifier.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

}

@XmlAccessorType(XmlAccessType.PROPERTY)
data class SwitchAnnouncement(
        var announcementType: AnnouncementType = AnnouncementType.Unknown,
        var switch: Switch = Switch(),
        var name: List<String> = emptyList()
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class Telemetry(
        var currentSensor: SensorType = SensorType.None,
        var currentSensorPage: Int = 0,
        var selectedSensor: List<SensorType> = emptyList(),
        var varioTone: Switch = Switch(),
        var voiceDelay: Int = 0,
        var voiceRepeat: Switch = Switch(),
        var voiceTrigger: Switch = Switch(),
        var varioToneSensor: VarioToneSensor = VarioToneSensor.Unknown,
        var userAlarmList: List<String> = emptyList(),
        var telemetryDataReceiveTime: Int = 0,
        var telemetryAlarmType: TelemetryAlarmType = TelemetryAlarmType.Off,
        var basicVoiceList: Int = 0,
        var generalAirVoiceList: Int = 0,
        var electricAirVoiceList: Int = 0,
        var varioVoiceList: Int = 0,
        var gpsVoiceList: Int = 0
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class TrainerConfig(
        @get:XmlIDREF
        var pupilChannel: List<Channel> = emptyList(),
        var pupilId: Long = 0L,
        @get:XmlIDREF
        var trainerSwitch: Switch = Switch(),
        @get:XmlIDREF
        var trainerChannel: List<Channel> = emptyList(),
        var trainerId: Long = 0L,
        var isWireless: Boolean = false
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class ThrottleCutOf(
        var position: Int = 0,
        @get:XmlIDREF
        var switch: Switch = Switch(),
        var threshold: Int = 0
) : AbstractBase()

@XmlAccessorType(XmlAccessType.PROPERTY)
data class ThrottleSettings(
        var throttleCutOf: ThrottleCutOf = ThrottleCutOf(),
        var throttleLastIdlePosition: Int = 0,
        var throttleTrim: Int = 0,
        var throttleIdleSwitch: Switch = Switch(),
        var throttleIdleLow: Int = 0
) : AbstractBase()
