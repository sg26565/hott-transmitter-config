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
package de.treichels.hott.model.winged;

import de.treichels.hott.model.BaseModel;
import de.treichels.hott.model.Channel;
import de.treichels.hott.model.Switch;
import de.treichels.hott.model.enums.AileronFlapType;
import de.treichels.hott.model.enums.ModelType;
import de.treichels.hott.model.enums.MotorOnC1Type;
import de.treichels.hott.model.enums.TailType;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
@XmlRootElement
public class WingedModel extends BaseModel {
    private static final long serialVersionUID = 1L;

    private AileronFlapType aileronFlapType;
    private Channel brakeInputChannel;
    private int brakeOffset;
    private boolean channel8Delay;
    private MotorOnC1Type motorOnC1Type;
    private WingedProfiTrim[] profiTrim;
    private Switch profiTrimSwitch;
    private TailType tailType;

    public WingedModel() {
        super(ModelType.Winged);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        final WingedModel other = (WingedModel) obj;
        if (aileronFlapType != other.aileronFlapType) return false;
        if (brakeInputChannel == null) {
            if (other.brakeInputChannel != null) return false;
        } else if (!brakeInputChannel.equals(other.brakeInputChannel)) return false;
        if (brakeOffset != other.brakeOffset) return false;
        if (channel8Delay != other.channel8Delay) return false;
        if (motorOnC1Type != other.motorOnC1Type) return false;
        if (!Arrays.equals(profiTrim, other.profiTrim)) return false;
        if (profiTrimSwitch == null) {
            if (other.profiTrimSwitch != null) return false;
        } else if (!profiTrimSwitch.equals(other.profiTrimSwitch)) return false;
        return tailType == other.tailType;
    }

    public AileronFlapType getAileronFlapType() {
        return aileronFlapType;
    }

    @XmlIDREF
    public Channel getBrakeInputChannel() {
        return brakeInputChannel;
    }

    public int getBrakeOffset() {
        return brakeOffset;
    }

    public MotorOnC1Type getMotorOnC1Type() {
        return motorOnC1Type;
    }

    @XmlElementWrapper(name = "profitrims")
    public WingedProfiTrim[] getProfiTrim() {
        return profiTrim;
    }

    @XmlIDREF
    public Switch getProfiTrimSwitch() {
        return profiTrimSwitch;
    }

    public TailType getTailType() {
        return tailType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (aileronFlapType == null ? 0 : aileronFlapType.hashCode());
        result = prime * result + (brakeInputChannel == null ? 0 : brakeInputChannel.hashCode());
        result = prime * result + brakeOffset;
        result = prime * result + (channel8Delay ? 1231 : 1237);
        result = prime * result + (motorOnC1Type == null ? 0 : motorOnC1Type.hashCode());
        result = prime * result + Arrays.hashCode(profiTrim);
        result = prime * result + (profiTrimSwitch == null ? 0 : profiTrimSwitch.hashCode());
        result = prime * result + (tailType == null ? 0 : tailType.hashCode());
        return result;
    }

    public boolean isChannel8Delay() {
        return channel8Delay;
    }

    public void setAileronFlapType(final AileronFlapType aileronFlapType) {
        this.aileronFlapType = aileronFlapType;
    }

    public void setBrakeInputChannel(final Channel brakeInputChannel) {
        this.brakeInputChannel = brakeInputChannel;
    }

    public void setBrakeOffset(final int brakeOffset) {
        this.brakeOffset = brakeOffset;
    }

    public void setChannel8Delay(final boolean channel8Delay) {
        this.channel8Delay = channel8Delay;
    }

    public void setMotorOnC1Type(final MotorOnC1Type motorOnC1Type) {
        this.motorOnC1Type = motorOnC1Type;
    }

    public void setProfiTrim(final WingedProfiTrim[] profiTrim) {
        this.profiTrim = profiTrim;
    }

    public void setProfiTrimSwitch(final Switch profiTrimSwitch) {
        this.profiTrimSwitch = profiTrimSwitch;
    }

    public void setTailType(final TailType tailType) {
        this.tailType = tailType;
    }
}
