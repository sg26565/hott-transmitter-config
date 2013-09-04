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
 *  along with this program.  If not, see <http://www.gnu.org/licenses/[].
 */
package gde.model.helicopter;

import gde.model.Curve;

/**
 * @author oli@treichels.de
 */
public class HelicopterMixer {
  private int   nick2TailMix;
  private int   nick2ThrottleMix;
  private Curve pitchCurve;
  private int   roll2TailMix;
  private int   roll2ThrottleMix;
  private int   swashplateLimit;
  private int   swashplateRotation;
  private int   tail2ThrottleMix;
  private Curve tailCurve;
  private Curve throttleCurve;

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
    final HelicopterMixer other = (HelicopterMixer) obj;
    if (nick2TailMix != other.nick2TailMix) {
      return false;
    }
    if (nick2ThrottleMix != other.nick2ThrottleMix) {
      return false;
    }
    if (pitchCurve == null) {
      if (other.pitchCurve != null) {
        return false;
      }
    } else if (!pitchCurve.equals(other.pitchCurve)) {
      return false;
    }
    if (roll2TailMix != other.roll2TailMix) {
      return false;
    }
    if (roll2ThrottleMix != other.roll2ThrottleMix) {
      return false;
    }
    if (swashplateLimit != other.swashplateLimit) {
      return false;
    }
    if (swashplateRotation != other.swashplateRotation) {
      return false;
    }
    if (tail2ThrottleMix != other.tail2ThrottleMix) {
      return false;
    }
    if (tailCurve == null) {
      if (other.tailCurve != null) {
        return false;
      }
    } else if (!tailCurve.equals(other.tailCurve)) {
      return false;
    }
    if (throttleCurve == null) {
      if (other.throttleCurve != null) {
        return false;
      }
    } else if (!throttleCurve.equals(other.throttleCurve)) {
      return false;
    }
    return true;
  }

  public int getNick2TailMix() {
    return nick2TailMix;
  }

  public int getNick2ThrottleMix() {
    return nick2ThrottleMix;
  }

  public Curve getPitchCurve() {
    return pitchCurve;
  }

  public int getRoll2TailMix() {
    return roll2TailMix;
  }

  public int getRoll2ThrottleMix() {
    return roll2ThrottleMix;
  }

  public int getSwashplateLimit() {
    return swashplateLimit;
  }

  public int getSwashplateRotation() {
    return swashplateRotation;
  }

  public int getTail2ThrottleMix() {
    return tail2ThrottleMix;
  }

  public Curve getTailCurve() {
    return tailCurve;
  }

  public Curve getThrottleCurve() {
    return throttleCurve;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + nick2TailMix;
    result = prime * result + nick2ThrottleMix;
    result = prime * result + (pitchCurve == null ? 0 : pitchCurve.hashCode());
    result = prime * result + roll2TailMix;
    result = prime * result + roll2ThrottleMix;
    result = prime * result + swashplateLimit;
    result = prime * result + swashplateRotation;
    result = prime * result + tail2ThrottleMix;
    result = prime * result + (tailCurve == null ? 0 : tailCurve.hashCode());
    result = prime * result + (throttleCurve == null ? 0 : throttleCurve.hashCode());
    return result;
  }

  public void setNick2TailMix(final int nick2TailMix) {
    this.nick2TailMix = nick2TailMix;
  }

  public void setNick2ThrottleMix(final int nick2ThrottleMix) {
    this.nick2ThrottleMix = nick2ThrottleMix;
  }

  public void setPitchCurve(final Curve pitchCurve) {
    this.pitchCurve = pitchCurve;
  }

  public void setRoll2TailMix(final int roll2TailMix) {
    this.roll2TailMix = roll2TailMix;
  }

  public void setRoll2ThrottleMix(final int roll2ThrottleMix) {
    this.roll2ThrottleMix = roll2ThrottleMix;
  }

  public void setSwashplateLimit(final int limit) {
    swashplateLimit = limit;
  }

  public void setSwashplateRotation(final int swashplateRotation) {
    this.swashplateRotation = swashplateRotation;
  }

  public void setTail2ThrottleMix(final int tail2ThrottleMix) {
    this.tail2ThrottleMix = tail2ThrottleMix;
  }

  public void setTailCurve(final Curve tailCurve) {
    this.tailCurve = tailCurve;
  }

  public void setThrottleCurve(final Curve throttleCurve) {
    this.throttleCurve = throttleCurve;
  }
}
