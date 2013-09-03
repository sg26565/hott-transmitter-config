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
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli
 * 
 */
public class Sequence {
  private boolean enabled;
  private int     number;
  private Channel outputChannel;
  private int[]   stepPosition;

  @XmlAttribute
  public int getNumber() {
    return number;
  }

  @XmlIDREF
  public Channel getOutputChannel() {
    return outputChannel;
  }

  public int[] getStepPosition() {
    return stepPosition;
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

  public void setOutputChannel(final Channel outputChannel) {
    this.outputChannel = outputChannel;
  }

  public void setStepPosition(final int[] stepPosition) {
    this.stepPosition = stepPosition;
  }
}
