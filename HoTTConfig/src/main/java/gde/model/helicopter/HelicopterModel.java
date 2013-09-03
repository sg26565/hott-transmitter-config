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

import gde.model.BaseModel;
import gde.model.Switch;
import gde.model.enums.ModelType;
import gde.model.enums.PitchMin;
import gde.model.enums.RotorDirection;
import gde.model.enums.SwashplateType;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author oli@treichels.de
 */
@XmlRootElement
public class HelicopterModel extends BaseModel {
  private int                   autorotationC1TriggerPosition;
  private Switch                autorotationSwitch;
  private int                   autorotationTailPosition;
  private int                   autorotationThrottlePosition;
  private int                   expoThrottleLimit;
  private Switch                markerSwitch;
  private PitchMin              pitchMin;
  private HelicopterProfiTrim[] profiTrim;
  private RotorDirection        rotorDirection;
  private boolean               swashplateLinearization;
  private SwashplateMix         swashplateMix;
  private SwashplateType        swashplateType;
  private int                   throttleLimitWarning;

  public HelicopterModel() {
    super(ModelType.Helicopter);
  }

  public int getAutorotationC1TriggerPosition() {
    return autorotationC1TriggerPosition;
  }

  @XmlIDREF
  public Switch getAutorotationSwitch() {
    return autorotationSwitch;
  }

  public int getAutorotationTailPosition() {
    return autorotationTailPosition;
  }

  public int getAutorotationThrottlePosition() {
    return autorotationThrottlePosition;
  }

  public int getExpoThrottleLimit() {
    return expoThrottleLimit;
  }

  @XmlIDREF
  public Switch getMarkerSwitch() {
    return markerSwitch;
  }

  public PitchMin getPitchMin() {
    return pitchMin;
  }

  @XmlElementWrapper(name = "profitrims")
  public HelicopterProfiTrim[] getProfiTrim() {
    return profiTrim;
  }

  public RotorDirection getRotorDirection() {
    return rotorDirection;
  }

  public SwashplateMix getSwashplateMix() {
    return swashplateMix;
  }

  public SwashplateType getSwashplateType() {
    return swashplateType;
  }

  public int getThrottleLimitWarning() {
    return throttleLimitWarning;
  }

  public boolean isSwashplateLinearization() {
    return swashplateLinearization;
  }

  public void setAutorotationC1TriggerPosition(final int autorotationPosition) {
    autorotationC1TriggerPosition = autorotationPosition;
  }

  public void setAutorotationSwitch(final Switch autorotatonSwitch) {
    autorotationSwitch = autorotatonSwitch;
  }

  public void setAutorotationTailPosition(final int autorotationTailPosition) {
    this.autorotationTailPosition = autorotationTailPosition;
  }

  public void setAutorotationThrottlePosition(final int autorotationThrottlePosition) {
    this.autorotationThrottlePosition = autorotationThrottlePosition;
  }

  public void setExpoThrottleLimit(final int expoThrottleLimit) {
    this.expoThrottleLimit = expoThrottleLimit;
  }

  public void setMarkerSwitch(final Switch markerSwitch) {
    this.markerSwitch = markerSwitch;
  }

  public void setPitchMin(final PitchMin pitchMin) {
    this.pitchMin = pitchMin;
  }

  public void setProfiTrim(final HelicopterProfiTrim[] profiTrim) {
    this.profiTrim = profiTrim;
  }

  public void setRotorDirection(final RotorDirection rotorDirection) {
    this.rotorDirection = rotorDirection;
  }

  public void setSwashplateLinearization(final boolean swashplateLinearization) {
    this.swashplateLinearization = swashplateLinearization;
  }

  public void setSwashplateMix(final SwashplateMix swashplateMix) {
    this.swashplateMix = swashplateMix;
  }

  public void setSwashplateType(final SwashplateType swashplateType) {
    this.swashplateType = swashplateType;
  }

  public void setThrottleLimitWarning(final int throttleLimitWarning) {
    this.throttleLimitWarning = throttleLimitWarning;
  }
}
