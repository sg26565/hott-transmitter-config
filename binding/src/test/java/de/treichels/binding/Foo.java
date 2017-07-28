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
package de.treichels.binding;

import java.util.Arrays;
import java.util.List;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 * 
 */
public class Foo {
  private String        stringValue;
  private int           integerValue;
  private boolean       booleanValue;
  private float         floatValue;
  private double        doubleValue;
  private Foo           objectValue;

  private String[]      stringArrayValue;
  private int[]         integerArrayValue;
  private boolean[]     booleanArrayValue;
  private float[]       floatArrayValue;
  private double[]      doubleArrayValue;
  private Foo[]         objectArrayValue;

  private List<String>  stringListValue;
  private List<Integer> integerListValue;
  private List<Boolean> booleanListValue;
  private List<Float>   floatListValue;
  private List<Double>  doubleListValue;
  private List<Foo>     objectListValue;

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
    final Foo other = (Foo) obj;
    if (!Arrays.equals(booleanArrayValue, other.booleanArrayValue)) {
      return false;
    }
    if (booleanListValue == null) {
      if (other.booleanListValue != null) {
        return false;
      }
    } else if (!booleanListValue.equals(other.booleanListValue)) {
      return false;
    }
    if (booleanValue != other.booleanValue) {
      return false;
    }
    if (!Arrays.equals(doubleArrayValue, other.doubleArrayValue)) {
      return false;
    }
    if (doubleListValue == null) {
      if (other.doubleListValue != null) {
        return false;
      }
    } else if (!doubleListValue.equals(other.doubleListValue)) {
      return false;
    }
    if (Double.doubleToLongBits(doubleValue) != Double.doubleToLongBits(other.doubleValue)) {
      return false;
    }
    if (!Arrays.equals(floatArrayValue, other.floatArrayValue)) {
      return false;
    }
    if (floatListValue == null) {
      if (other.floatListValue != null) {
        return false;
      }
    } else if (!floatListValue.equals(other.floatListValue)) {
      return false;
    }
    if (Float.floatToIntBits(floatValue) != Float.floatToIntBits(other.floatValue)) {
      return false;
    }
    if (!Arrays.equals(integerArrayValue, other.integerArrayValue)) {
      return false;
    }
    if (integerListValue == null) {
      if (other.integerListValue != null) {
        return false;
      }
    } else if (!integerListValue.equals(other.integerListValue)) {
      return false;
    }
    if (integerValue != other.integerValue) {
      return false;
    }
    if (!Arrays.equals(objectArrayValue, other.objectArrayValue)) {
      return false;
    }
    if (objectListValue == null) {
      if (other.objectListValue != null) {
        return false;
      }
    } else if (!objectListValue.equals(other.objectListValue)) {
      return false;
    }
    if (objectValue == null) {
      if (other.objectValue != null) {
        return false;
      }
    } else if (!objectValue.equals(other.objectValue)) {
      return false;
    }
    if (!Arrays.equals(stringArrayValue, other.stringArrayValue)) {
      return false;
    }
    if (stringListValue == null) {
      if (other.stringListValue != null) {
        return false;
      }
    } else if (!stringListValue.equals(other.stringListValue)) {
      return false;
    }
    if (stringValue == null) {
      if (other.stringValue != null) {
        return false;
      }
    } else if (!stringValue.equals(other.stringValue)) {
      return false;
    }
    return true;
  }

  public boolean[] getBooleanArrayValue() {
    return booleanArrayValue;
  }

  public List<Boolean> getBooleanListValue() {
    return booleanListValue;
  }

  public double[] getDoubleArrayValue() {
    return doubleArrayValue;
  }

  public List<Double> getDoubleListValue() {
    return doubleListValue;
  }

  public double getDoubleValue() {
    return doubleValue;
  }

  public float[] getFloatArrayValue() {
    return floatArrayValue;
  }

  public List<Float> getFloatListValue() {
    return floatListValue;
  }

  public float getFloatValue() {
    return floatValue;
  }

  public int[] getIntegerArrayValue() {
    return integerArrayValue;
  }

  public List<Integer> getIntegerListValue() {
    return integerListValue;
  }

  public int getIntegerValue() {
    return integerValue;
  }

  public Foo[] getObjectArrayValue() {
    return objectArrayValue;
  }

  public List<Foo> getObjectListValue() {
    return objectListValue;
  }

  public Foo getObjectValue() {
    return objectValue;
  }

  public String[] getStringArrayValue() {
    return stringArrayValue;
  }

  public List<String> getStringListValue() {
    return stringListValue;
  }

  public String getStringValue() {
    return stringValue;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(booleanArrayValue);
    result = prime * result + (booleanListValue == null ? 0 : booleanListValue.hashCode());
    result = prime * result + (booleanValue ? 1231 : 1237);
    result = prime * result + Arrays.hashCode(doubleArrayValue);
    result = prime * result + (doubleListValue == null ? 0 : doubleListValue.hashCode());
    long temp;
    temp = Double.doubleToLongBits(doubleValue);
    result = prime * result + (int) (temp ^ temp >>> 32);
    result = prime * result + Arrays.hashCode(floatArrayValue);
    result = prime * result + (floatListValue == null ? 0 : floatListValue.hashCode());
    result = prime * result + Float.floatToIntBits(floatValue);
    result = prime * result + Arrays.hashCode(integerArrayValue);
    result = prime * result + (integerListValue == null ? 0 : integerListValue.hashCode());
    result = prime * result + integerValue;
    result = prime * result + Arrays.hashCode(objectArrayValue);
    result = prime * result + (objectListValue == null ? 0 : objectListValue.hashCode());
    result = prime * result + (objectValue == null ? 0 : objectValue.hashCode());
    result = prime * result + Arrays.hashCode(stringArrayValue);
    result = prime * result + (stringListValue == null ? 0 : stringListValue.hashCode());
    result = prime * result + (stringValue == null ? 0 : stringValue.hashCode());
    return result;
  }

  public boolean isBooleanValue() {
    return booleanValue;
  }

  public void setBooleanArrayValue(final boolean[] booleanArrayValue) {
    this.booleanArrayValue = booleanArrayValue;
  }

  public void setBooleanListValue(final List<Boolean> booleanListValue) {
    this.booleanListValue = booleanListValue;
  }

  public void setBooleanValue(final boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  public void setDoubleArrayValue(final double[] doubleArrayValue) {
    this.doubleArrayValue = doubleArrayValue;
  }

  public void setDoubleListValue(final List<Double> doubleListValue) {
    this.doubleListValue = doubleListValue;
  }

  public void setDoubleValue(final double doubleValue) {
    this.doubleValue = doubleValue;
  }

  public void setFloatArrayValue(final float[] floatArrayValue) {
    this.floatArrayValue = floatArrayValue;
  }

  public void setFloatListValue(final List<Float> floatListValue) {
    this.floatListValue = floatListValue;
  }

  public void setFloatValue(final float floatValue) {
    this.floatValue = floatValue;
  }

  public void setIntegerArrayValue(final int[] integerArrayValue) {
    this.integerArrayValue = integerArrayValue;
  }

  public void setIntegerListValue(final List<Integer> integerListValue) {
    this.integerListValue = integerListValue;
  }

  public void setIntegerValue(final int integerValue) {
    this.integerValue = integerValue;
  }

  public void setObjectArrayValue(final Foo[] objectArrayValue) {
    this.objectArrayValue = objectArrayValue;
  }

  public void setObjectListValue(final List<Foo> objectListValue) {
    this.objectListValue = objectListValue;
  }

  public void setObjectValue(final Foo objectValue) {
    this.objectValue = objectValue;
  }

  public void setStringArrayValue(final String[] stringArrayValue) {
    this.stringArrayValue = stringArrayValue;
  }

  public void setStringListValue(final List<String> stringListValue) {
    this.stringListValue = stringListValue;
  }

  public void setStringValue(final String stringValue) {
    this.stringValue = stringValue;
  }
}
