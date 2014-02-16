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
public class LinearMixer extends CurveMixer {
  private static final long serialVersionUID = 1L;

  private int               offset;
  private int               travelHigh;
  private int               travelLow;

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
    final LinearMixer other = (LinearMixer) obj;
    if (offset != other.offset) {
      return false;
    }
    if (travelHigh != other.travelHigh) {
      return false;
    }
    if (travelLow != other.travelLow) {
      return false;
    }
    return true;
  }

  public int getOffset() {
    return offset;
  }

  public int getTravelHigh() {
    return travelHigh;
  }

  public int getTravelLow() {
    return travelLow;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + offset;
    result = prime * result + travelHigh;
    result = prime * result + travelLow;
    return result;
  }

  public void setOffset(final int offset) {
    this.offset = offset;
    updateCurve();
  }

  public void setTravelHigh(final int travelHigh) {
    this.travelHigh = travelHigh;
    updateCurve();
  }

  public void setTravelLow(final int travelLow) {
    this.travelLow = travelLow;
    updateCurve();
  }

  private void updateCurve() {
    final Curve curve = new Curve();
    final CurvePoint[] points = new CurvePoint[3];

    for (int i = 0; i < 3; i++) {
      final CurvePoint p = new CurvePoint();
      p.setNumber(i);
      p.setEnabled(true);
      points[i] = p;
    }

    points[0].setPosition(-100);
    points[1].setPosition(getOffset());
    points[2].setPosition(100);

    points[0].setValue((int) (-getTravelLow() * (1.0f + getOffset() / 100.0f)));
    points[1].setValue(0);
    points[2].setValue((int) (getTravelHigh() * (1.0f - getOffset() / 100.0f)));

    curve.setPoint(points);
    setCurve(curve);
  }
}