package gde.model.helicopter;

import gde.model.CurvePoint;
import gde.model.PhasedMixer;
import gde.model.SwashplateMix;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author oli@treichels.de
 */
public class HeliCopterMixer extends PhasedMixer {
	private int channel8Value;
	private int gyroGain;
	private List<CurvePoint> pitchCurve;
	private SwashplateMix swashplateMix;
	private List<CurvePoint> tailCurve;
	private List<CurvePoint> throttleCurve;

	public int getChannel8Value() {
		return channel8Value;
	}

	public int getGyroGain() {
		return gyroGain;
	}

	@XmlElement(name = "point")
	@XmlElementWrapper(name = "pitchcurve")
	public List<CurvePoint> getPitchCurve() {
		return pitchCurve;
	}

	public SwashplateMix getSwashplateMix() {
		return swashplateMix;
	}

	@XmlElement(name = "point")
	@XmlElementWrapper(name = "tailcurve")
	public List<CurvePoint> getTailCurve() {
		return tailCurve;
	}

	@XmlElement(name = "point")
	@XmlElementWrapper(name = "throttlecurve")
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

	public void setSwashplateMix(final SwashplateMix swashplateMix) {
		this.swashplateMix = swashplateMix;
	}

	public void setTailCurve(final List<CurvePoint> tailCurve) {
		this.tailCurve = tailCurve;
	}

	public void setThrottleCurve(final List<CurvePoint> throttleCurve) {
		this.throttleCurve = throttleCurve;
	}
}
