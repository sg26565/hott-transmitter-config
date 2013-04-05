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

/**
 * @author oli@treichels.de
 */
public class HelicopterMixer {
	private Curve pitchCurve;
	private int swashplateLimit;
	private int swashplateRotation;
	private Curve tailCurve;
	private Curve throttleCurve;

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

	public int getSwashplateRotation() {
		return swashplateRotation;
	}

	public void setSwashplateRotation(int swashplateRotation) {
		this.swashplateRotation = swashplateRotation;
	}
}
