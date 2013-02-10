package gde.model;

import java.util.List;

public class HeliCopterMixer extends PhasedMixer {
	private int channel8Value;
	private int gyroGain;
	private List<CurvePoint> pitchCurve;
	private int swashplateLimit;
	private List<CurvePoint> tailCurve;
	private List<CurvePoint> throttleCurve;

	public int getChannel8Value() {
		return channel8Value;
	}

	public int getGyroGain() {
		return gyroGain;
	}

	public List<CurvePoint> getPitchCurve() {
		return pitchCurve;
	}

	public int getSwashplateLimit() {
		return swashplateLimit;
	}

	public List<CurvePoint> getTailCurve() {
		return tailCurve;
	}

	public List<CurvePoint> getThrottleCurve() {
		return throttleCurve;
	}

	public void setChannel8Value(final int channel8Value) {
		this.channel8Value = channel8Value;
	}

	public void setGyroGain(final int gyroGain) {
		this.gyroGain = gyroGain;
	}

	public void setPitchCurve(final List<CurvePoint> pitchCurve) {
		this.pitchCurve = pitchCurve;
	}

	public void setSwashplateLimit(final int swashplateLimit) {
		this.swashplateLimit = swashplateLimit;
	}

	public void setTailCurve(final List<CurvePoint> tailCurve) {
		this.tailCurve = tailCurve;
	}

	public void setThrottleCurve(final List<CurvePoint> throttleCurve) {
		this.throttleCurve = throttleCurve;
	}
}
