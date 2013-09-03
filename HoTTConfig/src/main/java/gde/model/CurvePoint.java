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
