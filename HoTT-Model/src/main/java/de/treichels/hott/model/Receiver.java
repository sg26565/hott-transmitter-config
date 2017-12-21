/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.model;

import de.treichels.hott.model.enums.ReceiverFirmwareType;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import java.util.Arrays;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class Receiver extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private boolean bound;
    private ChannelMapping[] channelMapping;
    private String number;
    private long rfid;
    private boolean telemetry;
    private ReceiverFirmwareType firmwareType;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Receiver other = (Receiver) obj;
        if (bound != other.bound) return false;
        if (!Arrays.equals(channelMapping, other.channelMapping)) return false;
        if (number == null) {
            if (other.number != null) return false;
        } else if (!number.equals(other.number)) return false;
        if (rfid != other.rfid) return false;
        return telemetry == other.telemetry;
    }

    @XmlElementWrapper(name = "channelMappings")
    public ChannelMapping[] getChannelMapping() {
        return channelMapping;
    }

    public ReceiverFirmwareType getFirmwareType() {
        return firmwareType;
    }

    @XmlAttribute
    @XmlID
    public String getNumber() {
        return number;
    }

    public long getRfid() {
        return rfid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (bound ? 1231 : 1237);
        result = prime * result + Arrays.hashCode(channelMapping);
        result = prime * result + (number == null ? 0 : number.hashCode());
        result = prime * result + (int) (rfid ^ rfid >>> 32);
        result = prime * result + (telemetry ? 1231 : 1237);
        return result;
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

    public void setChannelMapping(final ChannelMapping[] channemMapping) {
        channelMapping = channemMapping;
    }

    public void setFirmwareType(final ReceiverFirmwareType firmwareType) {
        this.firmwareType = firmwareType;
    }

    public void setNumber(final int number) {
        this.number = Integer.toString(number);
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
