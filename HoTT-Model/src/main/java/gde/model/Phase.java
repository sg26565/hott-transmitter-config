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
import javax.xml.bind.annotation.XmlIDREF;

import gde.model.enums.PhaseType;

/**
 * @author oli@treichels.de
 */
public abstract class Phase extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private Curve channel1Curve;
    private Control[] control;
    private DualRateExpo[] dualRateExpo;
    private boolean motorOn;
    private String number;
    private String phaseName;
    private Switch phaseSwitch;
    private double phaseSwitchTime;
    private Clock phaseTimer;
    private PhaseType phaseType;
    private int[] digitalTrimValue;
    private int[] digitalTrimStep;
    private SwitchAnnouncement[] switchAnnouncements;
    private String phaseAnnouncement;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Phase other = (Phase) obj;
        if (channel1Curve == null) {
            if (other.channel1Curve != null) return false;
        } else if (!channel1Curve.equals(other.channel1Curve)) return false;
        if (!Arrays.equals(control, other.control)) return false;
        if (!Arrays.equals(dualRateExpo, other.dualRateExpo)) return false;
        if (motorOn != other.motorOn) return false;
        if (number == null) {
            if (other.number != null) return false;
        } else if (!number.equals(other.number)) return false;
        if (phaseName == null) {
            if (other.phaseName != null) return false;
        } else if (!phaseName.equals(other.phaseName)) return false;
        if (phaseSwitch == null) {
            if (other.phaseSwitch != null) return false;
        } else if (!phaseSwitch.equals(other.phaseSwitch)) return false;
        if (Double.doubleToLongBits(phaseSwitchTime) != Double.doubleToLongBits(other.phaseSwitchTime)) return false;
        if (phaseTimer == null) {
            if (other.phaseTimer != null) return false;
        } else if (!phaseTimer.equals(other.phaseTimer)) return false;
        if (phaseType != other.phaseType) return false;
        return true;
    }

    public Curve getChannel1Curve() {
        return channel1Curve;
    }

    @XmlElementWrapper(name = "controls")
    public Control[] getControl() {
        return control;
    }

    public int[] getDigitalTrimStep() {
        return digitalTrimStep;
    }

    public int[] getDigitalTrimValue() {
        return digitalTrimValue;
    }

    @XmlElementWrapper(name = "dualRateExpos")
    public DualRateExpo[] getDualRateExpo() {
        return dualRateExpo;
    }

    @XmlAttribute
    @XmlID
    public String getNumber() {
        return number;
    }

    public String getPhaseAnnouncement() {
        return phaseAnnouncement;
    }

    public String getPhaseName() {
        return phaseName;
    }

    @XmlIDREF
    public Switch getPhaseSwitch() {
        return phaseSwitch;
    }

    public double getPhaseSwitchTime() {
        return phaseSwitchTime;
    }

    @XmlIDREF
    public Clock getPhaseTimer() {
        return phaseTimer;
    }

    public PhaseType getPhaseType() {
        return phaseType;
    }

    public SwitchAnnouncement[] getSwitchAnnouncements() {
        return switchAnnouncements;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (channel1Curve == null ? 0 : channel1Curve.hashCode());
        result = prime * result + Arrays.hashCode(control);
        result = prime * result + Arrays.hashCode(dualRateExpo);
        result = prime * result + (motorOn ? 1231 : 1237);
        result = prime * result + (number == null ? 0 : number.hashCode());
        result = prime * result + (phaseName == null ? 0 : phaseName.hashCode());
        result = prime * result + (phaseSwitch == null ? 0 : phaseSwitch.hashCode());
        long temp;
        temp = Double.doubleToLongBits(phaseSwitchTime);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + (phaseTimer == null ? 0 : phaseTimer.hashCode());
        result = prime * result + (phaseType == null ? 0 : phaseType.hashCode());
        return result;
    }

    public boolean isMotorOn() {
        return motorOn;
    }

    public void setChannel1Curve(final Curve channel1Curve) {
        this.channel1Curve = channel1Curve;
    }

    public void setControl(final Control[] controls) {
        control = controls;
    }

    public void setDigitalTrimStep(final int[] digitalTrimStep) {
        this.digitalTrimStep = digitalTrimStep;
    }

    public void setDigitalTrimValue(final int[] digitalTrimValue) {
        this.digitalTrimValue = digitalTrimValue;
    }

    public void setDualRateExpo(final DualRateExpo[] dualRateExpo) {
        this.dualRateExpo = dualRateExpo;
    }

    public void setMotorOn(final boolean motorOn) {
        this.motorOn = motorOn;
    }

    public void setNumber(final int number) {
        this.number = Integer.toString(number);
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public void setPhaseAnnouncement(final String phaseAnnouncement) {
        this.phaseAnnouncement = phaseAnnouncement;
    }

    public void setPhaseName(final String phaseName) {
        this.phaseName = phaseName;
    }

    public void setPhaseSwitch(final Switch phaseSwitch) {
        this.phaseSwitch = phaseSwitch;
    }

    public void setPhaseSwitchTime(final double d) {
        phaseSwitchTime = d;
    }

    public void setPhaseTimer(final Clock phaseTimer) {
        this.phaseTimer = phaseTimer;
    }

    public void setPhaseType(final PhaseType phaseType) {
        this.phaseType = phaseType;
    }

    public void setSwitchAnnouncements(final SwitchAnnouncement[] switchAnnouncements) {
        this.switchAnnouncements = switchAnnouncements;
    }

    @Override
    public String toString() {
        return String.format("Phase %d: %s", Integer.parseInt(getNumber()) + 1, getPhaseName()); //$NON-NLS-1$
    }
}
