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

import gde.model.AbstractBase;

/**
 * @author oli@treichels.de
 */
public class HelicopterTrim extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private int nickStickTrim;
    private int rollStickTrim;
    private int tailStickTrim;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final HelicopterTrim other = (HelicopterTrim) obj;
        if (nickStickTrim != other.nickStickTrim) return false;
        if (rollStickTrim != other.rollStickTrim) return false;
        if (tailStickTrim != other.tailStickTrim) return false;
        return true;
    }

    public int getNickStickTrim() {
        return nickStickTrim;
    }

    public int getRollStickTrim() {
        return rollStickTrim;
    }

    public int getTailStickTrim() {
        return tailStickTrim;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + nickStickTrim;
        result = prime * result + rollStickTrim;
        result = prime * result + tailStickTrim;
        return result;
    }

    public void setNickStickTrim(final int nickStickTrim) {
        this.nickStickTrim = nickStickTrim;
    }

    public void setRollStickTrim(final int rollStickTrim) {
        this.rollStickTrim = rollStickTrim;
    }

    public void setTailStickTrim(final int tailStickTrim) {
        this.tailStickTrim = tailStickTrim;
    }
}
