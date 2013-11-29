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
package gde.report.html;

import gde.model.Curve;
import gde.report.CurveImageGenerator;

/**
 * @author oli
 * 
 */
public class DummyCurveImageGenerator implements CurveImageGenerator {
  /*
   * (non-Javadoc)
   * 
   * @see gde.report.CurveImageGenerator#getImageSource(gde.model.Curve, float,
   * boolean)
   */
  @Override
  public String getImageSource(final Curve curve, final float scale, final boolean description) {
    return "";
  }
}