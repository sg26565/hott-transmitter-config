/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package gde.model.helicopter;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import gde.model.BaseModel;
import gde.model.Switch;
import gde.model.enums.ModelType;
import gde.model.enums.PitchMin;
import gde.model.enums.RotorDirection;
import gde.model.enums.SwashplateType;

/**
 * @author oli@treichels.de
 */
@XmlRootElement
public class HelicopterModel extends BaseModel {
    private static final long serialVersionUID = 1L;

    private int autorotationC1TriggerPosition;
    private Switch autorotationSwitch;
    private int autorotationTailPosition;
    private int autorotationThrottlePosition;
    private int expoThrottleLimit;
    private Switch markerSwitch;
    private PitchMin pitchMin;
    private HelicopterProfiTrim[] profiTrim;
    private RotorDirection rotorDirection;
    private boolean swashplateLinearization;
    private SwashplateMix swashplateMix;
    private SwashplateType swashplateType;
    private int throttleLimitWarning;

    public HelicopterModel() {
        super(ModelType.Helicopter);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        final HelicopterModel other = (HelicopterModel) obj;
        if (autorotationC1TriggerPosition != other.autorotationC1TriggerPosition) return false;
        if (autorotationSwitch == null) {
            if (other.autorotationSwitch != null) return false;
        } else if (!autorotationSwitch.equals(other.autorotationSwitch)) return false;
        if (autorotationTailPosition != other.autorotationTailPosition) return false;
        if (autorotationThrottlePosition != other.autorotationThrottlePosition) return false;
        if (expoThrottleLimit != other.expoThrottleLimit) return false;
        if (markerSwitch == null) {
            if (other.markerSwitch != null) return false;
        } else if (!markerSwitch.equals(other.markerSwitch)) return false;
        if (pitchMin != other.pitchMin) return false;
        if (!Arrays.equals(profiTrim, other.profiTrim)) return false;
        if (rotorDirection != other.rotorDirection) return false;
        if (swashplateLinearization != other.swashplateLinearization) return false;
        if (swashplateMix == null) {
            if (other.swashplateMix != null) return false;
        } else if (!swashplateMix.equals(other.swashplateMix)) return false;
        if (swashplateType != other.swashplateType) return false;
        if (throttleLimitWarning != other.throttleLimitWarning) return false;
        return true;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + autorotationC1TriggerPosition;
        result = prime * result + (autorotationSwitch == null ? 0 : autorotationSwitch.hashCode());
        result = prime * result + autorotationTailPosition;
        result = prime * result + autorotationThrottlePosition;
        result = prime * result + expoThrottleLimit;
        result = prime * result + (markerSwitch == null ? 0 : markerSwitch.hashCode());
        result = prime * result + (pitchMin == null ? 0 : pitchMin.hashCode());
        result = prime * result + Arrays.hashCode(profiTrim);
        result = prime * result + (rotorDirection == null ? 0 : rotorDirection.hashCode());
        result = prime * result + (swashplateLinearization ? 1231 : 1237);
        result = prime * result + (swashplateMix == null ? 0 : swashplateMix.hashCode());
        result = prime * result + (swashplateType == null ? 0 : swashplateType.hashCode());
        result = prime * result + throttleLimitWarning;
        return result;
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
