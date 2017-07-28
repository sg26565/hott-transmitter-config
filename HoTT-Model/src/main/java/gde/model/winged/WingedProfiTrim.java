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
package gde.model.winged;

import javax.xml.bind.annotation.XmlIDREF;

import gde.model.AbstractBase;
import gde.model.Switch;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class WingedProfiTrim extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private boolean enabled;
    private Switch inputControl;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final WingedProfiTrim other = (WingedProfiTrim) obj;
        if (enabled != other.enabled) return false;
        if (inputControl == null) {
            if (other.inputControl != null) return false;
        } else if (!inputControl.equals(other.inputControl)) return false;
        return true;
    }

    @XmlIDREF
    public Switch getInputControl() {
        return inputControl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + (inputControl == null ? 0 : inputControl.hashCode());
        return result;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public void setInputControl(final Switch inputControl) {
        this.inputControl = inputControl;
    }
}
