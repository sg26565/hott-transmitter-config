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

import gde.model.enums.MixerType;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlIDREF;

/**
 * Kreuzmischer
 * 
 * @author oli
 */
public class DualMixer extends AbstractBase {
  private static final long serialVersionUID = 1L;

  private Channel[]         channel;
  private int               diff;
  private int               number;
  private MixerType         type;

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
    final DualMixer other = (DualMixer) obj;
    if (!Arrays.equals(channel, other.channel)) {
      return false;
    }
    if (diff != other.diff) {
      return false;
    }
    if (number != other.number) {
      return false;
    }
    if (type != other.type) {
      return false;
    }
    return true;
  }

  @XmlIDREF
  public Channel[] getChannel() {
    return channel;
  }

  public int getDiff() {
    return diff;
  }

  public int getNumber() {
    return number;
  }

  public MixerType getType() {
    return type;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(channel);
    result = prime * result + diff;
    result = prime * result + number;
    result = prime * result + (type == null ? 0 : type.hashCode());
    return result;
  }

  public void setChannel(final Channel[] channel) {
    this.channel = channel;
  }

  public void setDiff(final int diff) {
    this.diff = diff;
  }

  public void setNumber(final int number) {
    this.number = number;
  }

  public void setType(final MixerType type) {
    this.type = type;
  }
}
