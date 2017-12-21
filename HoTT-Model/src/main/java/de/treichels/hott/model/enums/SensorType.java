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
package de.treichels.hott.model.enums;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public enum SensorType {
    ElectricAirModule(1 << 2), ESC(1 << 5), GeneralModule(1 << 1), GPS(1 << 4), None(0), Receiver(1 << 0), Vario(1 << 3);

    public static Collection<SensorType> forCode(final int code) {
        return Stream.of(SensorType.values()).filter(s -> (s.getCode() & code) != 0).collect(Collectors.toList());
    }

    public static int getCode(final Collection<SensorType> sensors) {
        return sensors.stream().mapToInt(SensorType::getCode).sum();
    }

    private final int code;

    SensorType(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
