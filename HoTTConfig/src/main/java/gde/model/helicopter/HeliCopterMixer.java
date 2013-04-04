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
package gde.model.helicopter;

import gde.model.Curve;
import gde.model.PhasedMixer;

/**
 * @author oli@treichels.de
 */
public class HeliCopterMixer extends PhasedMixer {
	private int channel8Value;
	private int gyroGain;
	private Curve pitchCurve;
	private int swashplateLimit;
	private Curve tailCurve;
	private Curve throttleCurve;
	private int 

	public int getChannel8Value() {
		return channel8Value;
	}

	public int getGyroGain() {
		return gyroGain;
	}

	public Curve getPitchCurve() {
		return pitchCurve;
	}

	public int getSwashplateLimit() {
		return swashplateLimit;
	}

	public Curve getTailCurve() {
		return tailCurve;
	}

	public Curve getThrottleCurve() {
		return throttleCurve;
	}

	public void setChannel8Value(final int channel8Value) {
		this.channel8Value = channel8Value;
	}

	public void setGyroGain(final int gyroGain) {
		this.gyroGain = gyroGain;
	}

	public void setPitchCurve(final Curve pitchCurve) {
		this.pitchCurve = pitchCurve;
	}

	public void setSwashplateLimit(final int limit) {
		swashplateLimit = limit;
	}

	public void setTailCurve(final Curve tailCurve) {
		this.tailCurve = tailCurve;
	}

	public void setThrottleCurve(final Curve throttleCurve) {
		this.throttleCurve = throttleCurve;
	}
}
