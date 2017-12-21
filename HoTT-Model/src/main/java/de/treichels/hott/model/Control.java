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
package de.treichels.hott.model;

import de.treichels.hott.model.enums.ControlMode;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class Control extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private Switch inputControl;
    private ControlMode mode;
    private String number;
    private int offset;
    private double timeHigh;
    private double timeLow;
    private Switch toggleHighSwitch;
    private Switch toggleLowSwitch;
    private int travelHigh;
    private int travelLow;
    private int trim;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Control other = (Control) obj;
        if (inputControl == null) {
            if (other.inputControl != null) return false;
        } else if (!inputControl.equals(other.inputControl)) return false;
        if (mode != other.mode) return false;
        if (number == null) {
            if (other.number != null) return false;
        } else if (!number.equals(other.number)) return false;
        if (offset != other.offset) return false;
        if (Double.doubleToLongBits(timeHigh) != Double.doubleToLongBits(other.timeHigh)) return false;
        if (Double.doubleToLongBits(timeLow) != Double.doubleToLongBits(other.timeLow)) return false;
        if (toggleHighSwitch == null) {
            if (other.toggleHighSwitch != null) return false;
        } else if (!toggleHighSwitch.equals(other.toggleHighSwitch)) return false;
        if (toggleLowSwitch == null) {
            if (other.toggleLowSwitch != null) return false;
        } else if (!toggleLowSwitch.equals(other.toggleLowSwitch)) return false;
        if (travelHigh != other.travelHigh) return false;
        if (travelLow != other.travelLow) return false;
        return trim == other.trim;
    }

    @XmlIDREF
    public Switch getInputControl() {
        return inputControl;
    }

    public ControlMode getMode() {
        return mode;
    }

    @XmlAttribute
    @XmlID
    public String getNumber() {
        return number;
    }

    public int getOffset() {
        return offset;
    }

    public double getTimeHigh() {
        return timeHigh;
    }

    public double getTimeLow() {
        return timeLow;
    }

    @XmlIDREF
    public Switch getToggleHighSwitch() {
        return toggleHighSwitch;
    }

    @XmlIDREF
    public Switch getToggleLowSwitch() {
        return toggleLowSwitch;
    }

    public int getTravelHigh() {
        return travelHigh;
    }

    public int getTravelLow() {
        return travelLow;
    }

    public int getTrim() {
        return trim;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (inputControl == null ? 0 : inputControl.hashCode());
        result = prime * result + (mode == null ? 0 : mode.hashCode());
        result = prime * result + (number == null ? 0 : number.hashCode());
        result = prime * result + offset;
        long temp;
        temp = Double.doubleToLongBits(timeHigh);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(timeLow);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + (toggleHighSwitch == null ? 0 : toggleHighSwitch.hashCode());
        result = prime * result + (toggleLowSwitch == null ? 0 : toggleLowSwitch.hashCode());
        result = prime * result + travelHigh;
        result = prime * result + travelLow;
        result = prime * result + trim;
        return result;
    }

    public void setInputControl(final Switch controlSwitch) {
        inputControl = controlSwitch;
    }

    public void setMode(final ControlMode mode) {
        this.mode = mode;
    }

    public void setNumber(final int number) {
        this.number = Integer.toString(number);
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public void setOffset(final int offset) {
        this.offset = offset;
    }

    public void setTimeHigh(final double timeHigh) {
        this.timeHigh = timeHigh;
    }

    public void setTimeLow(final double timeLow) {
        this.timeLow = timeLow;
    }

    public void setToggleHighSwitch(final Switch leftSwitch) {
        toggleHighSwitch = leftSwitch;
    }

    public void setToggleLowSwitch(final Switch rightSwitch) {
        toggleLowSwitch = rightSwitch;
    }

    public void setTravelHigh(final int travelHigh) {
        this.travelHigh = travelHigh;
    }

    public void setTravelLow(final int travelLow) {
        this.travelLow = travelLow;
    }

    public void setTrim(final int trim) {
        this.trim = trim;
    }
}
