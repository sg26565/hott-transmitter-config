package de.treichels.hott.model

import de.treichels.hott.model.enums.*
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlIDREF
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
open class HelicopterModel(
        var autorotationC1TriggerPosition: Int = 0,
        @get:XmlIDREF
        var autorotationSwitch: Switch = Switch(),
        var autorotationTailPosition: Int = 0,
        var autorotationThrottlePosition: Int = 0,
        var expoThrottleLimit: Int = 0,
        @get:XmlIDREF
        var markerSwitch: Switch = Switch(),
        var pitchMin: PitchMin = PitchMin.Unknown,
        @get:XmlElementWrapper(name = "profitrims")
        var profiTrim: List<HelicopterProfiTrim> = emptyList(),
        var rotorDirection: RotorDirection = RotorDirection.Unknown,
        var isSwashplateLinearization: Boolean = false,
        var swashplateMix: SwashplateMix = SwashplateMix(),
        var swashplateType: SwashplateType = SwashplateType.Unknown,
        var throttleLimitWarning: Int = 0,
        var isThrottleMarkerActive: Boolean = false,
        var throttleMarkerPosition: Int = 0
) : BaseModel(ModelType.Helicopter) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HelicopterModel) return false
        if (!super.equals(other)) return false

        if (autorotationC1TriggerPosition != other.autorotationC1TriggerPosition) return false
        if (autorotationSwitch != other.autorotationSwitch) return false
        if (autorotationTailPosition != other.autorotationTailPosition) return false
        if (autorotationThrottlePosition != other.autorotationThrottlePosition) return false
        if (expoThrottleLimit != other.expoThrottleLimit) return false
        if (markerSwitch != other.markerSwitch) return false
        if (pitchMin != other.pitchMin) return false
        if (profiTrim != other.profiTrim) return false
        if (rotorDirection != other.rotorDirection) return false
        if (isSwashplateLinearization != other.isSwashplateLinearization) return false
        if (swashplateMix != other.swashplateMix) return false
        if (swashplateType != other.swashplateType) return false
        if (throttleLimitWarning != other.throttleLimitWarning) return false
        if (isThrottleMarkerActive != other.isThrottleMarkerActive) return false
        if (throttleMarkerPosition != other.throttleMarkerPosition) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + autorotationC1TriggerPosition
        result = 31 * result + autorotationSwitch.hashCode()
        result = 31 * result + autorotationTailPosition
        result = 31 * result + autorotationThrottlePosition
        result = 31 * result + expoThrottleLimit
        result = 31 * result + markerSwitch.hashCode()
        result = 31 * result + pitchMin.hashCode()
        result = 31 * result + profiTrim.hashCode()
        result = 31 * result + rotorDirection.hashCode()
        result = 31 * result + isSwashplateLinearization.hashCode()
        result = 31 * result + swashplateMix.hashCode()
        result = 31 * result + swashplateType.hashCode()
        result = 31 * result + throttleLimitWarning
        result = 31 * result + isThrottleMarkerActive.hashCode()
        result = 31 * result + throttleMarkerPosition
        return result
    }
}

data class HelicopterMixer(
        var nick2TailMix: Int = 0,
        var nick2ThrottleMix: Int = 0,
        var pitchCurve: Curve = Curve(),
        var roll2TailMix: Int = 0,
        var roll2ThrottleMix: Int = 0,
        var swashplateLimit: Int = 0,
        var swashplateRotation: Int = 0,
        var tail2ThrottleMix: Int = 0,
        var tailCurve: Curve = Curve(),
        var throttleCurve: Curve = Curve(),
        var roll2NickSwitch: Switch = Switch(), // TODO for nick2Roll, pitch2Roll, pitch2Nick
        var roll2NickLeft: Int = 0,
        var roll2NickRight: Int = 0,
        var roll2NickExpLow: Int = 0,
        var roll2NickExpHigh: Int = 0,
        var pitch2TailCurve: Curve = Curve()

) : AbstractBase()

class HelicopterPhase(
        var channel8Value: Int = 0,
        var gyroGain: Int = 0,
        var gyroSuppression: Int = 0,
        var helicopterMixer: HelicopterMixer = HelicopterMixer(),
        var helicopterTrim: HelicopterTrim = HelicopterTrim()
) : Phase() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HelicopterPhase) return false
        if (!super.equals(other)) return false

        if (channel8Value != other.channel8Value) return false
        if (gyroGain != other.gyroGain) return false
        if (gyroSuppression != other.gyroSuppression) return false
        if (helicopterMixer != other.helicopterMixer) return false
        if (helicopterTrim != other.helicopterTrim) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + channel8Value
        result = 31 * result + gyroGain
        result = 31 * result + gyroSuppression
        result = 31 * result + helicopterMixer.hashCode()
        result = 31 * result + helicopterTrim.hashCode()
        return result
    }
}

data class HelicopterProfiTrim(
        var curveType: CurveType = CurveType.Unknown,
        @get:XmlIDREF
        var inputControl: Switch = Switch(),
        @get:XmlIDREF
        var phase: HelicopterPhase = HelicopterPhase(),
        var point: Int = 0
) : AbstractBase()

data class HelicopterTrim(
        var nickStickTrim: Int = 0,
        var rollStickTrim: Int = 0,
        var tailStickTrim: Int = 0
) : AbstractBase()

data class SwashplateMix(
        var nickMix: Int = 0,
        var pitchMix: Int = 0,
        var rollMix: Int = 0
) : AbstractBase()
