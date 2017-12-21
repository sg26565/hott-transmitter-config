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

import de.treichels.hott.model.enums.AnnouncementType;

public class SwitchAnnouncement extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private AnnouncementType announcementType;
    private Switch sw;
    private String[] name;

    public AnnouncementType getAnnouncementType() {
        return announcementType;
    }

    public String[] getName() {
        return name;
    }

    public Switch getSwitch() {
        return sw;
    }

    public void setAnnouncementType(final AnnouncementType announcmentType) {
        announcementType = announcmentType;
    }

    public void setName(final String[] name) {
        this.name = name;
    }

    public void setSwitch(final Switch sw) {
        this.sw = sw;
    }
}
