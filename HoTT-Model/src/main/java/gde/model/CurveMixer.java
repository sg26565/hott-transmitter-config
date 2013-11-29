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
 * 
 */
public class CurveMixer extends FreeMixer {
  private Curve curve;

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final CurveMixer other = (CurveMixer) obj;
    if (curve == null) {
      if (other.curve != null) {
        return false;
      }
    } else if (!curve.equals(other.curve)) {
      return false;
    }
    return true;
  }

  public Curve getCurve() {
    return curve;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + (curve == null ? 0 : curve.hashCode());
    return result;
  }

  public void setCurve(final Curve curve) {
    this.curve = curve;
  }
}
