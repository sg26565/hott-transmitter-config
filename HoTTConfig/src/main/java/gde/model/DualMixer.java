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
package gde.model;

import gde.model.enums.MixerType;

import javax.xml.bind.annotation.XmlIDREF;

/**
 * Kreuzmischer
 * 
 * @author oli
 */
public class DualMixer {
	private Channel[] channel;
	private int diff;
	private int number;
	private MixerType type;

	@XmlIDREF
	public Channel[] getChannel() {
		return channel;
	}

	public int getDiff() {
		return diff;
	}

	public int getNumber() {
		return number;
	}

	public MixerType getType() {
		return type;
	}

	public void setChannel(final Channel[] channel) {
		this.channel = channel;
	}

	public void setDiff(final int diff) {
		this.diff = diff;
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	public void setType(final MixerType type) {
		this.type = type;
	}
}
