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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;

/**
 * @author oli
 */
public class Receiver {
	private boolean bound;
	private ChannelMapping[] channemMapping;
	private String number;
	private long rfid;
	private boolean telemetry;

	public Receiver(final int number) {
		this.number = Integer.toString(number);
	}

	@XmlElement(name = "channelMapping")
	public ChannelMapping[] getChannemMapping() {
		return channemMapping;
	}

	@XmlAttribute
	@XmlID
	public String getNumber() {
		return number;
	}

	public long getRfid() {
		return rfid;
	}

	public boolean isBound() {
		return bound;
	}

	public boolean isTelemetry() {
		return telemetry;
	}

	public void setBound(final boolean bound) {
		this.bound = bound;
	}

	public void setChannemMapping(final ChannelMapping[] channemMapping) {
		this.channemMapping = channemMapping;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public void setRfid(final long id) {
		rfid = id;
	}

	public void setTelemetry(final boolean telemetry) {
		this.telemetry = telemetry;
	}
}
