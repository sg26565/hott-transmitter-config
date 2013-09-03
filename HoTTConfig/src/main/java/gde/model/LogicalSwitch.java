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

import gde.model.enums.LogicalSwitchMode;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli
 * 
 */
public class LogicalSwitch extends Switch {
  private boolean           enabled;
  private LogicalSwitchMode mode;
  private Switch[]          sw;

  public LogicalSwitchMode getMode() {
    return mode;
  }

  @XmlElementWrapper(name = "switches")
  @XmlIDREF
  public Switch[] getSwitch() {
    return sw;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(final boolean enables) {
    enabled = enables;
  }

  public void setMode(final LogicalSwitchMode mode) {
    this.mode = mode;
  }

  public void setSwitch(final Switch[] switches) {
    sw = switches;
  }
}
