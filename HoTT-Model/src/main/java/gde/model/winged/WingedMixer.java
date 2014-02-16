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
package gde.model.winged;

import gde.model.AbstractBase;
import gde.model.Switch;
import gde.model.enums.SwitchFunction;

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

public class WingedMixer extends AbstractBase {
  private static final long serialVersionUID = 1L;

  private SwitchFunction    function;
  private Object[]          qualifier;
  private Switch            sw;
  private int[]             value;

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
    final WingedMixer other = (WingedMixer) obj;
    if (function != other.function) {
      return false;
    }
    if (!Arrays.equals(qualifier, other.qualifier)) {
      return false;
    }
    if (sw == null) {
      if (other.sw != null) {
        return false;
      }
    } else if (!sw.equals(other.sw)) {
      return false;
    }
    if (!Arrays.equals(value, other.value)) {
      return false;
    }
    return true;
  }

  public SwitchFunction getFunction() {
    return function;
  }

  @XmlID
  @XmlAttribute
  public String getId() {
    final StringBuilder b = new StringBuilder();

    b.append(function.name());

    if (qualifier != null) {
      for (final Object q : qualifier) {
        b.append("_");

        if (q.getClass().isEnum()) {
          try {
            final Method m = q.getClass().getMethod("name");
            b.append(m.invoke(q));
          } catch (final Exception e) {
            throw new RuntimeException(e);
          }
        } else {
          b.append(q.toString());
        }
      }
    }

    return b.toString();
  }

  public Object[] getQualifier() {
    return qualifier;
  }

  @XmlIDREF
  public Switch getSwitch() {
    return sw;
  }

  public int[] getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (function == null ? 0 : function.hashCode());
    result = prime * result + Arrays.hashCode(qualifier);
    result = prime * result + (sw == null ? 0 : sw.hashCode());
    result = prime * result + Arrays.hashCode(value);
    return result;
  }

  public void setFunction(final SwitchFunction function) {
    this.function = function;
  }

  public void setQualifier(final Object[] qualifier) {
    this.qualifier = qualifier;
  }

  public void setSwitch(final Switch sw) {
    this.sw = sw;
  }

  public void setValue(final int value) {
    final int[] values = new int[1];
    values[0] = value;

    setValue(values);
  }

  public void setValue(final int[] value) {
    this.value = value;
  }
}
