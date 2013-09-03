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
public class DualRateExpo {
  DualRate dualRate;
  Expo     expo;

  public Curve[] getCurve() {
    final Curve[] curve = new Curve[4];

    for (int i = 0; i < 4; i++) {
      final CurvePoint[] points = new CurvePoint[5];
      curve[i] = new Curve();
      curve[i].setPoint(points);
      curve[i].setSmoothing(true);

      for (int j = 0; j < 5; j++) {
        final CurvePoint p = new CurvePoint();
        p.setNumber(j);
        p.setEnabled(true);
        points[j] = p;
      }

      final int dr = getDualRate().getValues()[i / 2];
      final int expo = (int) (getExpo().getValues()[i % 2] / 3f);

      points[0].setPosition(-100);
      points[0].setValue(-dr);

      points[1].setPosition(-50);
      points[1].setValue((-50 + expo) * dr / 100);

      points[2].setPosition(0);
      points[2].setValue(0);

      points[3].setPosition(50);
      points[3].setValue((50 - expo) * dr / 100);

      points[4].setPosition(100);
      points[4].setValue(dr);
    }

    return curve;
  }

  public DualRate getDualRate() {
    return dualRate;
  }

  public Expo getExpo() {
    return expo;
  }

  public void setDualRate(final DualRate dualRate) {
    this.dualRate = dualRate;
  }

  public void setExpo(final Expo expo) {
    this.expo = expo;
  }
}
