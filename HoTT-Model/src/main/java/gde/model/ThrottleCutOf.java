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

import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class ThrottleCutOf extends AbstractBase {
  private int    position;
  private Switch sw;
  private int    threshold;

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
    final ThrottleCutOf other = (ThrottleCutOf) obj;
    if (position != other.position) {
      return false;
    }
    if (sw == null) {
      if (other.sw != null) {
        return false;
      }
    } else if (!sw.equals(other.sw)) {
      return false;
    }
    if (threshold != other.threshold) {
      return false;
    }
    return true;
  }

  public int getPosition() {
    return position;
  }

  @XmlIDREF
  public Switch getSwitch() {
    return sw;
  }

  public int getThreshold() {
    return threshold;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + position;
    result = prime * result + (sw == null ? 0 : sw.hashCode());
    result = prime * result + threshold;
    return result;
  }

  public void setPosition(final int position) {
    this.position = position;
  }

  public void setSwitch(final Switch sw) {
    this.sw = sw;
  }

  public void setThreshold(final int threshold) {
    this.threshold = threshold;
  }
}
