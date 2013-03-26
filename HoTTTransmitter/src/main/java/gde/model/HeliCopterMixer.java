package gde.model;

/**
 * @author oli@treichels.de
 */
public class HeliCopterMixer extends PhasedMixer {
	private int channel8Value;
	private int gyroGain;
	private CurvePoints pitchCurve;
	private Swashplate swashplate;
	private CurvePoints tailCurve;
	private CurvePoints throttleCurve;

	public int getChannel8Value() {
		return channel8Value;
	}

	public int getGyroGain() {
		return gyroGain;
	}

	public CurvePoints getPitchCurve() {
		return pitchCurve;
	}

	public CurvePoints getTailCurve() {
		return tailCurve;
	}

	public CurvePoints getThrottleCurve() {
		return throttleCurve;
	}

	public void setChannel8Value(final int channel8Value) {
		this.channel8Value = channel8Value;
	}

	public void setGyroGain(final int gyroGain) {
		this.gyroGain = gyroGain;
	}

	public void setPitchCurve(final CurvePoints pitchCurve) {
		this.pitchCurve = pitchCurve;
	}

	public void setTailCurve(final CurvePoints tailCurve) {
		this.tailCurve = tailCurve;
	}

	public void setThrottleCurve(final CurvePoints throttleCurve) {
		this.throttleCurve = throttleCurve;
	}

	public Swashplate getSwashplate() {
		return swashplate;
	}

	public void setSwashplate(final Swashplate swashplate) {
		this.swashplate = swashplate;
	}
}
