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

import gde.model.enums.AnnouncementType;

public class SwitchAnnouncement extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private AnnouncementType announcmentType;
    private Switch sw;
    private int[] soundIndex;

    public int[] getSoundIndex() {
        return soundIndex;
    }

    public Switch getSwitch() {
        return sw;
    }

    public void setSoundIndex(final int[] soundIndex) {
        this.soundIndex = soundIndex;
    }

    public void setSwitch(final Switch sw) {
        this.sw = sw;
    }

    public AnnouncementType getAnnouncmentType() {
        return announcmentType;
    }

    public void setAnnouncmentType(AnnouncementType announcmentType) {
        this.announcmentType = announcmentType;
    }
}
