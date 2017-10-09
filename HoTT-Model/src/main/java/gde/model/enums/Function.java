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
package gde.model.enums;

import java.util.ResourceBundle;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public enum Function {
    Aileron, Brake, Delta, Elevator, Flap, Nick, Pitch, Reduction, Roll, Rudder, TailRotor, Throttle, Unknown, VTail, Gyro;

    /** @return the locale-dependent message */
    @Override
    public String toString() {
        return ResourceBundle.getBundle(getClass().getName()).getString(name());
    }
}
