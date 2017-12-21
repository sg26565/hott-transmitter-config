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

import de.treichels.hott.model.enums.MixerInputType;
import de.treichels.hott.model.enums.MixerType;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import java.util.Arrays;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public abstract class FreeMixer extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private Channel fromChannel;
    private MixerInputType inputType;
    private String number;
    private FreeMixerPhaseSetting[] phaseSetting;
    private Switch sw;
    private Channel toChannel;
    private MixerType type;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final FreeMixer other = (FreeMixer) obj;
        if (fromChannel == null) {
            if (other.fromChannel != null) return false;
        } else if (!fromChannel.equals(other.fromChannel)) return false;
        if (inputType != other.inputType) return false;
        if (number == null) {
            if (other.number != null) return false;
        } else if (!number.equals(other.number)) return false;
        if (!Arrays.equals(phaseSetting, other.phaseSetting)) return false;
        if (sw == null) {
            if (other.sw != null) return false;
        } else if (!sw.equals(other.sw)) return false;
        if (toChannel == null) {
            if (other.toChannel != null) return false;
        } else if (!toChannel.equals(other.toChannel)) return false;
        return type == other.type;
    }

    @XmlIDREF
    public Channel getFromChannel() {
        return fromChannel;
    }

    public MixerInputType getInputType() {
        return inputType;
    }

    @XmlAttribute
    @XmlID
    public String getNumber() {
        return number;
    }

    @XmlElementWrapper(name = "phaseSettings")
    public FreeMixerPhaseSetting[] getPhaseSetting() {
        return phaseSetting;
    }

    @XmlIDREF
    public Switch getSwitch() {
        return sw;
    }

    @XmlIDREF
    public Channel getToChannel() {
        return toChannel;
    }

    public MixerType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (fromChannel == null ? 0 : fromChannel.hashCode());
        result = prime * result + (inputType == null ? 0 : inputType.hashCode());
        result = prime * result + (number == null ? 0 : number.hashCode());
        result = prime * result + Arrays.hashCode(phaseSetting);
        result = prime * result + (sw == null ? 0 : sw.hashCode());
        result = prime * result + (toChannel == null ? 0 : toChannel.hashCode());
        result = prime * result + (type == null ? 0 : type.hashCode());
        return result;
    }

    public void setFromChannel(final Channel from) {
        fromChannel = from;
    }

    public void setInputType(final MixerInputType inputType) {
        this.inputType = inputType;
    }

    public void setNumber(final int number) {
        this.number = Integer.toString(number);
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public void setPhaseSetting(final FreeMixerPhaseSetting[] phaseSetting) {
        this.phaseSetting = phaseSetting;
    }

    public void setSwitch(final Switch sw) {
        this.sw = sw;
    }

    public void setToChannel(final Channel to) {
        toChannel = to;
    }

    public void setType(final MixerType type) {
        this.type = type;
    }
}
