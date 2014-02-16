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

import java.util.Arrays;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli
 */
public class PhaseAssignment extends AbstractBase {
  private static final long serialVersionUID = 1L;

  private Phase[]           assignment;
  private Switch            combiCSwitch;
  private Switch            combiDSwitch;
  private Switch            combiESwitch;
  private Switch            combiFSwitch;
  private Switch            priorityASwitch;
  private Switch            priorityBSwitch;

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
    final PhaseAssignment other = (PhaseAssignment) obj;
    if (!Arrays.equals(assignment, other.assignment)) {
      return false;
    }
    if (combiCSwitch == null) {
      if (other.combiCSwitch != null) {
        return false;
      }
    } else if (!combiCSwitch.equals(other.combiCSwitch)) {
      return false;
    }
    if (combiDSwitch == null) {
      if (other.combiDSwitch != null) {
        return false;
      }
    } else if (!combiDSwitch.equals(other.combiDSwitch)) {
      return false;
    }
    if (combiESwitch == null) {
      if (other.combiESwitch != null) {
        return false;
      }
    } else if (!combiESwitch.equals(other.combiESwitch)) {
      return false;
    }
    if (combiFSwitch == null) {
      if (other.combiFSwitch != null) {
        return false;
      }
    } else if (!combiFSwitch.equals(other.combiFSwitch)) {
      return false;
    }
    if (priorityASwitch == null) {
      if (other.priorityASwitch != null) {
        return false;
      }
    } else if (!priorityASwitch.equals(other.priorityASwitch)) {
      return false;
    }
    if (priorityBSwitch == null) {
      if (other.priorityBSwitch != null) {
        return false;
      }
    } else if (!priorityBSwitch.equals(other.priorityBSwitch)) {
      return false;
    }
    return true;
  }

  @XmlIDREF
  @XmlElementWrapper(name = "assignments")
  public Phase[] getAssignment() {
    return assignment;
  }

  @XmlIDREF
  public Switch getCombiCSwitch() {
    return combiCSwitch;
  }

  @XmlIDREF
  public Switch getCombiDSwitch() {
    return combiDSwitch;
  }

  @XmlIDREF
  public Switch getCombiESwitch() {
    return combiESwitch;
  }

  @XmlIDREF
  public Switch getCombiFSwitch() {
    return combiFSwitch;
  }

  public Phase getCombiPhase(final boolean c, final boolean d, final boolean e, final boolean f) {
    final int number = (c ? 8 : 0) + (d ? 4 : 0) + (e ? 2 : 0) + (f ? 1 : 0);

    return getCombiPhase(number);
  }

  public Phase getCombiPhase(final int number) {
    if (number == 0) {
      return getNormalPhase();
    } else {
      return assignment[number + 2];
    }
  }

  public Phase getNormalPhase() {
    return assignment[0];
  }

  public Phase getPriorityAPhase() {
    return assignment[1];
  }

  @XmlIDREF
  public Switch getPriorityASwitch() {
    return priorityASwitch;
  }

  public Phase getPriorityBPhase() {
    return assignment[2];
  }

  @XmlIDREF
  public Switch getPriorityBSwitch() {
    return priorityBSwitch;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(assignment);
    result = prime * result + (combiCSwitch == null ? 0 : combiCSwitch.hashCode());
    result = prime * result + (combiDSwitch == null ? 0 : combiDSwitch.hashCode());
    result = prime * result + (combiESwitch == null ? 0 : combiESwitch.hashCode());
    result = prime * result + (combiFSwitch == null ? 0 : combiFSwitch.hashCode());
    result = prime * result + (priorityASwitch == null ? 0 : priorityASwitch.hashCode());
    result = prime * result + (priorityBSwitch == null ? 0 : priorityBSwitch.hashCode());
    return result;
  }

  public void setAssignment(final Phase[] assignment) {
    this.assignment = assignment;
  }

  public void setCombiCSwitch(final Switch combiCSwitch) {
    this.combiCSwitch = combiCSwitch;
  }

  public void setCombiDSwitch(final Switch combiDSwitch) {
    this.combiDSwitch = combiDSwitch;
  }

  public void setCombiESwitch(final Switch combiESwitch) {
    this.combiESwitch = combiESwitch;
  }

  public void setCombiFSwitch(final Switch combiFSwitch) {
    this.combiFSwitch = combiFSwitch;
  }

  public void setPriorityASwitch(final Switch priorityASwitch) {
    this.priorityASwitch = priorityASwitch;
  }

  public void setPriorityBSwitch(final Switch priorityBSwitch) {
    this.priorityBSwitch = priorityBSwitch;
  }
}
