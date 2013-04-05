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

import gde.model.Phase;

/**
 * @author oli
 * 
 */
public class HelicopterPhase extends Phase {
	private int channel8Value;
	private int gyroGain;
	private HelicopterMixer helicopterMixer;
	private HelicopterTrim helicopterTrim;

	public int getChannel8Value() {
		return channel8Value;
	}

	public int getGyroGain() {
		return gyroGain;
	}

	public HelicopterMixer getHelicopterMixer() {
		return helicopterMixer;
	}

	public HelicopterTrim getHelicopterTrim() {
		return helicopterTrim;
	}

	public void setChannel8Value(final int channel8Value) {
		this.channel8Value = channel8Value;
	}

	public void setGyroGain(final int gyroGain) {
		this.gyroGain = gyroGain;
	}

	public void setHelicopterMixer(final HelicopterMixer heliMix) {
		helicopterMixer = heliMix;
	}

	public void setHelicopterTrim(final HelicopterTrim strickTrim) {
		helicopterTrim = strickTrim;
	}
}
