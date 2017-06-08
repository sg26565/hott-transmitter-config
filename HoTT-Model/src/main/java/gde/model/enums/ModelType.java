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
import java.util.stream.Stream;

/**
 * @author oli@treichels.de
 */
public enum ModelType {
    Helicopter(0, 'h'), Unknown(-1, 'x'), Winged(1, 'a'), Copter(2, 'q'), Boat(3, 'b'), Car(4, 'c'); // TODO: verify ids

    public static ModelType forChar(final char c) {
        return Stream.of(values()).filter(t -> t.c == c).findFirst().orElse(Unknown);
    }

    public static ModelType forId(final int id) {
        return Stream.of(values()).filter(t -> t.id == id).findFirst().orElse(Unknown);
    }

    private final int id;
    private final char c;

    private ModelType(final int id, final char c) {
        this.id = id;
        this.c = c;
    }

    public char getChar() {
        return c;
    }

    public int getId() {
        return id;
    }

    /** @return the locale-dependent message */
    @Override
    public String toString() {
        return ResourceBundle.getBundle(getClass().getName()).getString(name());
    }
}
