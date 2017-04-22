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

public class LapStore extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private boolean isActive;
    private int viewLap;
    private int currentLap;
    private Lap[] laps;

    public int getCurrentLap() {
        return currentLap;
    }

    public Lap[] getLaps() {
        return laps;
    }

    public int getViewLap() {
        return viewLap;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public void setCurrentLap(final int currentLap) {
        this.currentLap = currentLap;
    }

    public void setLaps(final Lap[] laps) {
        this.laps = laps;
    }

    public void setViewLap(final int viewLap) {
        this.viewLap = viewLap;
    }
}
