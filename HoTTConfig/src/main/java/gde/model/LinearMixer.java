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

/**
 * @author oli
 * 
 */
public class LinearMixer extends FreeMixer {
	private int offset;
	private int travelHigh;
	private int travelLow;

	public int getOffset() {
		return offset;
	}

	public int getTravelHigh() {
		return travelHigh;
	}

	public int getTravelLow() {
		return travelLow;
	}

	public void setOffset(final int offset) {
		this.offset = offset;
	}

	public void setTravelHigh(final int travelHigh) {
		this.travelHigh = travelHigh;
	}

	public void setTravelLow(final int travelLow) {
		this.travelLow = travelLow;
	}
}
