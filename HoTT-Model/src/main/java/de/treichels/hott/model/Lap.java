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

public class Lap extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private int minute;
    private int second;
    private int millisecond;

    public int getMillisecond() {
        return millisecond;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public void setMillisecond(final int millisecond) {
        this.millisecond = millisecond;
    }

    public void setMinute(final int minute) {
        this.minute = minute;
    }

    public void setSecond(final int second) {
        this.second = second;
    }
}
