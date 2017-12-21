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

import de.treichels.hott.model.enums.SequenceStatus;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import java.util.Arrays;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class ChannelSequencer extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private int maxStep;
    private SequenceStatus powerOffStatus;
    private Sequence[] sequence;
    private double[] stepTime;
    private Switch sw;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ChannelSequencer other = (ChannelSequencer) obj;
        if (maxStep != other.maxStep) return false;
        if (powerOffStatus != other.powerOffStatus) return false;
        if (!Arrays.equals(sequence, other.sequence)) return false;
        if (!Arrays.equals(stepTime, other.stepTime)) return false;
        if (sw == null) {
            return other.sw == null;
        } else return sw.equals(other.sw);
    }

    public int getMaxStep() {
        return maxStep;
    }

    public SequenceStatus getPowerOffStatus() {
        return powerOffStatus;
    }

    @XmlElementWrapper(name = "sequences")
    public Sequence[] getSequence() {
        return sequence;
    }

    @XmlElementWrapper(name = "stepTimes")
    public double[] getStepTime() {
        return stepTime;
    }

    @XmlIDREF
    public Switch getSwitch() {
        return sw;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + maxStep;
        result = prime * result + (powerOffStatus == null ? 0 : powerOffStatus.hashCode());
        result = prime * result + Arrays.hashCode(sequence);
        result = prime * result + Arrays.hashCode(stepTime);
        result = prime * result + (sw == null ? 0 : sw.hashCode());
        return result;
    }

    public void setMaxStep(final int maxStep) {
        this.maxStep = maxStep;
    }

    public void setPowerOffStatus(final SequenceStatus powerOffStatus) {
        this.powerOffStatus = powerOffStatus;
    }

    public void setSequence(final Sequence[] sequence) {
        this.sequence = sequence;
    }

    public void setStepTime(final double[] stepTime) {
        this.stepTime = stepTime;
    }

    public void setSwitch(final Switch sw) {
        this.sw = sw;
    }
}
