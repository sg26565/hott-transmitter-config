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

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;

import gde.model.enums.LogicalSwitchMode;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public class LogicalSwitch extends Switch {
    private static final long serialVersionUID = 1L;

    private boolean enabled;
    private LogicalSwitchMode mode;
    private Switch[] sw;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        final LogicalSwitch other = (LogicalSwitch) obj;
        if (enabled != other.enabled) return false;
        if (mode != other.mode) return false;
        if (!Arrays.equals(sw, other.sw)) return false;
        return true;
    }

    public LogicalSwitchMode getMode() {
        return mode;
    }

    @XmlElementWrapper(name = "switches")
    @XmlIDREF
    public Switch[] getSwitch() {
        return sw;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + (mode == null ? 0 : mode.hashCode());
        result = prime * result + Arrays.hashCode(sw);
        return result;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enables) {
        enabled = enables;
    }

    public void setMode(final LogicalSwitchMode mode) {
        this.mode = mode;
    }

    public void setSwitch(final Switch[] switches) {
        sw = switches;
    }
}
