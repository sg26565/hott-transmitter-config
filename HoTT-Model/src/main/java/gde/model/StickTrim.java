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

import gde.model.enums.TrimMode;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author oli
 */
public class StickTrim extends AbstractBase {
  private int      channel;
  private int      increment;
  private TrimMode mode;
  private int      timeHigh;
  private int      timeLow;

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final StickTrim other = (StickTrim) obj;
    if (channel != other.channel) {
      return false;
    }
    if (increment != other.increment) {
      return false;
    }
    if (mode != other.mode) {
      return false;
    }
    if (timeHigh != other.timeHigh) {
      return false;
    }
    if (timeLow != other.timeLow) {
      return false;
    }
    return true;
  }

  @XmlAttribute
  public int getChannel() {
    return channel;
  }

  public int getIncrement() {
    return increment;
  }

  public TrimMode getMode() {
    return mode;
  }

  public int getTimeHigh() {
    return timeHigh;
  }

  public int getTimeLow() {
    return timeLow;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + channel;
    result = prime * result + increment;
    result = prime * result + (mode == null ? 0 : mode.hashCode());
    result = prime * result + timeHigh;
    result = prime * result + timeLow;
    return result;
  }

  public void setChannel(final int channel) {
    this.channel = channel;
  }

  public void setIncrement(final int increment) {
    this.increment = increment;
  }

  public void setMode(final TrimMode mode) {
    this.mode = mode;
  }

  public void setTimeHigh(final int timeHigh) {
    this.timeHigh = timeHigh;
  }

  public void setTimeLow(final int timeLow) {
    this.timeLow = timeLow;
  }
}
