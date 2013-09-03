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
package gde.model;

import java.util.Collection;

import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class TrainerConfig {
  private Collection<Channel> pupilChannel;
  private long                pupilId;
  private Switch              sw;
  private Collection<Channel> trainerChannel;
  private long                trainerId;
  private boolean             wireless;

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