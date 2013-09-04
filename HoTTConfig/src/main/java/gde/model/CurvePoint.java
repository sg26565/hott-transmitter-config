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

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author oli@treichels.de
 */
public class CurvePoint {
  private boolean enabled;
  private int     number;
  private int     position;
  private int     value;

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
    final CurvePoint other = (CurvePoint) obj;
    if (enabled != other.enabled) {
      return false;
    }
    if (number != other.number) {
      return false;
    }
    if (position != other.position) {
      return false;
    }
    if (value != other.value) {
      return false;
    }
    return true;
  }

  @XmlAttribute
  public int getNumber() {
    return number;
  }

  public int getPosition() {
    return position;
  }

  public int getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (enabled ? 1231 : 1237);
    result = prime * result + number;
    result = prime * result + position;
    result = prime * result + value;
    return result;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  public void setNumber(final int number) {
    this.number = number;
  }

  public void setPosition(final int position) {
    this.position = position;
  }

  public void setValue(final int value) {
    this.value = value;
  }
}
