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

import gde.model.enums.MultichannelMode;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli
 */
public class Multichannel extends AbstractBase {
  private static final long serialVersionUID = 1L;

  private Control[]         control;
  private boolean           enabled;
  private Channel           inputChannel;
  private MultichannelMode  mode;
  private int               number;

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
    final Multichannel other = (Multichannel) obj;
    if (!Arrays.equals(control, other.control)) {
      return false;
    }
    if (enabled != other.enabled) {
      return false;
    }
    if (inputChannel == null) {
      if (other.inputChannel != null) {
        return false;
      }
    } else if (!inputChannel.equals(other.inputChannel)) {
      return false;
    }
    if (mode != other.mode) {
      return false;
    }
    if (number != other.number) {
      return false;
    }
    return true;
  }

  @XmlElementWrapper(name = "controls")
  public Control[] getControl() {
    return control;
  }

  @XmlIDREF
  public Channel getInputChannel() {
    return inputChannel;
  }

  public MultichannelMode getMode() {
    return mode;
  }

  @XmlAttribute
  public int getNumber() {
    return number;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(control);
    result = prime * result + (enabled ? 1231 : 1237);
    result = prime * result + (inputChannel == null ? 0 : inputChannel.hashCode());
    result = prime * result + (mode == null ? 0 : mode.hashCode());
    result = prime * result + number;
    return result;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setControl(final Control[] control) {
    this.control = control;
  }

  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  public void setInputChannel(final Channel inputChannel) {
    this.inputChannel = inputChannel;
  }

  public void setMode(final MultichannelMode mode) {
    this.mode = mode;
  }

  public void setNumber(final int number) {
    this.number = number;
  }
}
