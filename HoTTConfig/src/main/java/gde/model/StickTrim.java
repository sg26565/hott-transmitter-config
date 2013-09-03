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
public class StickTrim {
  private int      channel;
  private int      increment;
  private TrimMode mode;
  private int      timeHigh;
  private int      timeLow;

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
