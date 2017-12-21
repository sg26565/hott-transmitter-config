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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public class ChannelPhaseSetting extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private boolean nonDelayed;
    private Phase phase;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ChannelPhaseSetting other = (ChannelPhaseSetting) obj;
        if (nonDelayed != other.nonDelayed) return false;
        if (phase == null) {
            return other.phase == null;
        } else return phase.equals(other.phase);
    }

    @XmlIDREF
    @XmlAttribute
    public Phase getPhase() {
        return phase;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (nonDelayed ? 1231 : 1237);
        result = prime * result + (phase == null ? 0 : phase.hashCode());
        return result;
    }

    @XmlAttribute
    public boolean isNonDelayed() {
        return nonDelayed;
    }

    public void setNonDelayed(final boolean nonDelayed) {
        this.nonDelayed = nonDelayed;
    }

    public void setPhase(final Phase phase) {
        this.phase = phase;
    }
}
