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

import javax.xml.bind.annotation.XmlIDREF;

import gde.model.AbstractBase;
import gde.model.Switch;
import gde.model.enums.CurveType;

/**
 * @author oli
 */
public class HelicopterProfiTrim extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private CurveType curveType;
    private Switch inputControl;
    private HelicopterPhase phase;
    private int point;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final HelicopterProfiTrim other = (HelicopterProfiTrim) obj;
        if (curveType != other.curveType) return false;
        if (inputControl == null) {
            if (other.inputControl != null) return false;
        } else if (!inputControl.equals(other.inputControl)) return false;
        if (phase == null) {
            if (other.phase != null) return false;
        } else if (!phase.equals(other.phase)) return false;
        if (point != other.point) return false;
        return true;
    }

    public CurveType getCurveType() {
        return curveType;
    }

    @XmlIDREF
    public Switch getInputControl() {
        return inputControl;
    }

    @XmlIDREF
    public HelicopterPhase getPhase() {
        return phase;
    }

    public int getPoint() {
        return point;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (curveType == null ? 0 : curveType.hashCode());
        result = prime * result + (inputControl == null ? 0 : inputControl.hashCode());
        result = prime * result + (phase == null ? 0 : phase.hashCode());
        result = prime * result + point;
        return result;
    }

    public void setCurveType(final CurveType curveType) {
        this.curveType = curveType;
    }

    public void setInputControl(final Switch inputControl) {
        this.inputControl = inputControl;
    }

    public void setPhase(final HelicopterPhase phase) {
        this.phase = phase;
    }

    public void setPoint(final int point) {
        this.point = point;
    }
}
