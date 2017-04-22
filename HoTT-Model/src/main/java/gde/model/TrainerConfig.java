/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/[].
 */
package gde.model;

import java.util.Collection;

import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class TrainerConfig extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private Collection<Channel> pupilChannel;
    private long pupilId;
    private Switch sw;
    private Collection<Channel> trainerChannel;
    private long trainerId;
    private boolean wireless;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final TrainerConfig other = (TrainerConfig) obj;
        if (pupilChannel == null) {
            if (other.pupilChannel != null) return false;
        } else if (!pupilChannel.equals(other.pupilChannel)) return false;
        if (pupilId != other.pupilId) return false;
        if (sw == null) {
            if (other.sw != null) return false;
        } else if (!sw.equals(other.sw)) return false;
        if (trainerChannel == null) {
            if (other.trainerChannel != null) return false;
        } else if (!trainerChannel.equals(other.trainerChannel)) return false;
        if (trainerId != other.trainerId) return false;
        if (wireless != other.wireless) return false;
        return true;
    }

    @XmlIDREF
    public Collection<Channel> getPupilChannel() {
        return pupilChannel;
    }

    public long getPupilId() {
        return pupilId;
    }

    @XmlIDREF
    public Collection<Channel> getTrainerChannel() {
        return trainerChannel;
    }

    public long getTrainerId() {
        return trainerId;
    }

    @XmlIDREF
    public Switch getTrainerSwitch() {
        return sw;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (pupilChannel == null ? 0 : pupilChannel.hashCode());
        result = prime * result + (int) (pupilId ^ pupilId >>> 32);
        result = prime * result + (sw == null ? 0 : sw.hashCode());
        result = prime * result + (trainerChannel == null ? 0 : trainerChannel.hashCode());
        result = prime * result + (int) (trainerId ^ trainerId >>> 32);
        result = prime * result + (wireless ? 1231 : 1237);
        return result;
    }

    public boolean isWireless() {
        return wireless;
    }

    public void setPupilChannel(final Collection<Channel> pupilChannels) {
        pupilChannel = pupilChannels;
    }

    public void setPupilId(final long pupilId) {
        this.pupilId = pupilId;
    }

    public void setTrainerChannel(final Collection<Channel> trainerChannels) {
        trainerChannel = trainerChannels;
    }

    public void setTrainerId(final long trainerId) {
        this.trainerId = trainerId;
    }

    public void setTrainerSwitch(final Switch sw) {
        this.sw = sw;
    }

    public void setWireless(final boolean wireless) {
        this.wireless = wireless;
    }
}