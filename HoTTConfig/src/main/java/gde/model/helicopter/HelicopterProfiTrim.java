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
package gde.model.helicopter;

import gde.model.Switch;
import gde.model.enums.CurveType;

import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli
 */
public class HelicopterProfiTrim {
  private CurveType       curveType;
  private Switch          inputControl;
  private HelicopterPhase phase;
  private int             point;

  public CurveType getCurveType() {
    return curveType;
  }

  @XmlIDREF
  public Switch getInputControl() {
    return inputControl;
  }

  @XmlIDREF
  public HelicopterPhase getPhase() {
    return phase;
  }

  public int getPoint() {
    return point;
  }

  public void setCurveType(final CurveType curveType) {
    this.curveType = curveType;
  }

  public void setInputControl(final Switch inputControl) {
    this.inputControl = inputControl;
  }

  public void setPhase(final HelicopterPhase phase) {
    this.phase = phase;
  }

  public void setPoint(final int point) {
    this.point = point;
  }
}
