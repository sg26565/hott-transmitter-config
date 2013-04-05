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
	private int Nich2TailMix;
	private int Nick2ThrottleMix;
	private Curve pitchCurve;
	private int roll2TailMix;
	private int roll2ThrottleMix;
	private int swashplateLimit;
	private int swashplateRotation;
	private int tail2ThrottleMix;
	private Curve tailCurve;
	private Curve throttleCurve;

	public int getNich2TailMix() {
		return Nich2TailMix;
	}

	public int getNick2ThrottleMix() {
		return Nick2ThrottleMix;
	}

	public Curve getPitchCurve() {
		return pitchCurve;
	}

	public int getRoll2TailMix() {
		return roll2TailMix;
	}

	public int getRoll2ThrottleMix() {
		return roll2ThrottleMix;
	}

	public int getSwashplateLimit() {
		return swashplateLimit;
	}

	public int getSwashplateRotation() {
		return swashplateRotation;
	}

	public int getTail2ThrottleMix() {
		return tail2ThrottleMix;
	}

	public Curve getTailCurve() {
		return tailCurve;
	}

	public Curve getThrottleCurve() {
		return throttleCurve;
	}

	public void setNich2TailMix(final int nich2TailMix) {
		Nich2TailMix = nich2TailMix;
	}

	public void setNick2ThrottleMix(final int nick2ThrottleMix) {
		Nick2ThrottleMix = nick2ThrottleMix;
	}

	public void setPitchCurve(final Curve pitchCurve) {
		this.pitchCurve = pitchCurve;
	}

	public void setRoll2TailMix(final int roll2TailMix) {
		this.roll2TailMix = roll2TailMix;
	}

	public void setRoll2ThrottleMix(final int roll2ThrottleMix) {
		this.roll2ThrottleMix = roll2ThrottleMix;
	}

	public void setSwashplateLimit(final int limit) {
		swashplateLimit = limit;
	}

	public void setSwashplateRotation(final int swashplateRotation) {
		this.swashplateRotation = swashplateRotation;
	}

	public void setTail2ThrottleMix(final int tail2ThrottleMix) {
		this.tail2ThrottleMix = tail2ThrottleMix;
	}

	public void setTailCurve(final Curve tailCurve) {
		this.tailCurve = tailCurve;
	}

	public void setThrottleCurve(final Curve throttleCurve) {
		this.throttleCurve = throttleCurve;
	}
}
