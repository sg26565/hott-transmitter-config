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

import de.treichels.hott.model.enums.CurveType;

import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.Arrays;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public class Curve extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private CurvePoint[] point;
    private boolean smoothing = false;
    private CurveType type;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Curve other = (Curve) obj;
        if (!Arrays.equals(point, other.point)) return false;
        if (smoothing != other.smoothing) return false;
        return type == other.type;
    }

    @XmlElementWrapper(name = "points")
    public CurvePoint[] getPoint() {
        return point;
    }

    public CurveType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(point);
        result = prime * result + (smoothing ? 1231 : 1237);
        result = prime * result + (type == null ? 0 : type.hashCode());
        return result;
    }

    public boolean isSmoothing() {
        return smoothing;
    }

    public void setPoint(final CurvePoint[] point) {
        this.point = point;
    }

    public void setSmoothing(final boolean smoothing) {
        this.smoothing = smoothing;
    }

    public void setType(final CurveType type) {
        this.type = type;
    }
}
