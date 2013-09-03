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

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli
 */
public class PhaseAssignment {
  private Phase[] assignment;
  private Switch  combiCSwitch;
  private Switch  combiDSwitch;
  private Switch  combiESwitch;
  private Switch  combiFSwitch;
  private Switch  priorityASwitch;
  private Switch  priorityBSwitch;

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
