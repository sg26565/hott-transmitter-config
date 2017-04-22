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
package gde.model;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;

import gde.model.enums.FailSafeMode;
import gde.model.enums.Function;
import gde.model.enums.TrainerMode;

/**
 * @author oli@treichels.de
 */
public class Channel extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private int center;
    private FailSafeMode failSafeMode;
    private int failSafePosition;
    private Function function;
    private int limitHigh;
    private int limitLow;
    private boolean mixOnly;
    private String number;
    private ChannelPhaseSetting[] phaseSetting;
    private boolean reverse;
    private TrainerMode trainerMode;
    private int travelHigh;
    private int travelLow;
    private boolean virtual = false;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Channel other = (Channel) obj;
        if (center != other.center) return false;
        if (failSafeMode != other.failSafeMode) return false;
        if (failSafePosition != other.failSafePosition) return false;
        if (function != other.function) return false;
        if (limitHigh != other.limitHigh) return false;
        if (limitLow != other.limitLow) return false;
        if (mixOnly != other.mixOnly) return false;
        if (number == null) {
            if (other.number != null) return false;
        } else if (!number.equals(other.number)) return false;
        if (!Arrays.equals(phaseSetting, other.phaseSetting)) return false;
        if (reverse != other.reverse) return false;
        if (trainerMode != other.trainerMode) return false;
        if (travelHigh != other.travelHigh) return false;
        if (travelLow != other.travelLow) return false;
        if (virtual != other.virtual) return false;
        return true;
    }

    public int getCenter() {
        return center;
    }

    public FailSafeMode getFailSafeMode() {
        return failSafeMode;
    }

    public int getFailSafePosition() {
        return failSafePosition;
    }

    public Function getFunction() {
        return function;
    }

    public int getLimitHigh() {
        return limitHigh;
    }

    public int getLimitLow() {
        return limitLow;
    }

    @XmlAttribute
    @XmlID
    public String getNumber() {
        return number;
    }

    @XmlElementWrapper(name = "phaseSettings")
    public ChannelPhaseSetting[] getPhaseSetting() {
        return phaseSetting;
    }

    public TrainerMode getTrainerMode() {
        return trainerMode;
    }

    public int getTravelHigh() {
        return travelHigh;
    }

    public int getTravelLow() {
        return travelLow;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + center;
        result = prime * result + (failSafeMode == null ? 0 : failSafeMode.hashCode());
        result = prime * result + failSafePosition;
        result = prime * result + (function == null ? 0 : function.hashCode());
        result = prime * result + limitHigh;
        result = prime * result + limitLow;
        result = prime * result + (mixOnly ? 1231 : 1237);
        result = prime * result + (number == null ? 0 : number.hashCode());
        result = prime * result + Arrays.hashCode(phaseSetting);
        result = prime * result + (reverse ? 1231 : 1237);
        result = prime * result + (trainerMode == null ? 0 : trainerMode.hashCode());
        result = prime * result + travelHigh;
        result = prime * result + travelLow;
        result = prime * result + (virtual ? 1231 : 1237);
        return result;
    }

    public boolean isMixOnly() {
        return mixOnly;
    }

    public boolean isReverse() {
        return reverse;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setCenter(final int center) {
        this.center = center;
    }

    public void setFailSafeMode(final FailSafeMode failSafeMode) {
        this.failSafeMode = failSafeMode;
    }

    public void setFailSafePosition(final int failSafePosition) {
        this.failSafePosition = failSafePosition;
    }

    public void setFunction(final Function function) {
        this.function = function;
    }

    public void setLimitHigh(final int limitHigh) {
        this.limitHigh = limitHigh;
    }

    public void setLimitLow(final int limitLow) {
        this.limitLow = limitLow;
    }

    public void setMixOnly(final boolean mixOnly) {
        this.mixOnly = mixOnly;
    }

    public void setNumber(final int number) {
        this.number = Integer.toString(number);
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public void setPhaseSetting(final ChannelPhaseSetting[] delayed) {
        phaseSetting = delayed;
    }

    public void setReverse(final boolean reverse) {
        this.reverse = reverse;
    }

    public void setTrainerMode(final TrainerMode trainerMode) {
        this.trainerMode = trainerMode;
    }

    public void setTravelHigh(final int travelHigh) {
        this.travelHigh = travelHigh;
    }

    public void setTravelLow(final int travelLow) {
        this.travelLow = travelLow;
    }

    public void setVirtual(final boolean virtual) {
        this.virtual = virtual;
    }
}
