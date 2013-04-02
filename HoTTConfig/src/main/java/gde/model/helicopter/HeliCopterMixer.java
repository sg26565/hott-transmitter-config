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
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
