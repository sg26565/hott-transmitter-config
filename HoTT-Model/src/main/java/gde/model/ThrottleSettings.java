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

/**
 * @author oli
 */
public class ThrottleSettings extends AbstractBase {
  private ThrottleCutOf throttleCutOf;
  private int           throttleLastIdlePosition;
  private int           throttleTrim;

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
    final ThrottleSettings other = (ThrottleSettings) obj;
    if (throttleCutOf == null) {
      if (other.throttleCutOf != null) {
        return false;
      }
    } else if (!throttleCutOf.equals(other.throttleCutOf)) {
      return false;
    }
    if (throttleLastIdlePosition != other.throttleLastIdlePosition) {
      return false;
    }
    if (throttleTrim != other.throttleTrim) {
      return false;
    }
    return true;
  }

  public ThrottleCutOf getThrottleCutOf() {
    return throttleCutOf;
  }

  public int getThrottleLastIdlePosition() {
    return throttleLastIdlePosition;
  }

  public int getThrottleTrim() {
    return throttleTrim;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (throttleCutOf == null ? 0 : throttleCutOf.hashCode());
    result = prime * result + throttleLastIdlePosition;
    result = prime * result + throttleTrim;
    return result;
  }

  public void setThrottleCutOf(final ThrottleCutOf throttleCutOf) {
    this.throttleCutOf = throttleCutOf;
  }

  public void setThrottleLastIdlePosition(final int throttleLastPosition) {
    throttleLastIdlePosition = throttleLastPosition;
  }

  public void setThrottleTrim(final int throttleTrim) {
    this.throttleTrim = throttleTrim;
  }

}
