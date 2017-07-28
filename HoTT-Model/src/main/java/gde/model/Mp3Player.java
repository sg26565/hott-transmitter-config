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

import gde.model.enums.Mp3PlayerMode;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class Mp3Player extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private int album;
    private Mp3PlayerMode mode;
    private Switch playPauseSwitch;
    private int track;
    private int volume;
    private Switch volumeLeftSwitch;
    private Switch volumeRightSwitch;
    private Switch volumeSwitch;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Mp3Player other = (Mp3Player) obj;
        if (album != other.album) return false;
        if (mode != other.mode) return false;
        if (playPauseSwitch == null) {
            if (other.playPauseSwitch != null) return false;
        } else if (!playPauseSwitch.equals(other.playPauseSwitch)) return false;
        if (track != other.track) return false;
        if (volume != other.volume) return false;
        if (volumeLeftSwitch == null) {
            if (other.volumeLeftSwitch != null) return false;
        } else if (!volumeLeftSwitch.equals(other.volumeLeftSwitch)) return false;
        if (volumeRightSwitch == null) {
            if (other.volumeRightSwitch != null) return false;
        } else if (!volumeRightSwitch.equals(other.volumeRightSwitch)) return false;
        if (volumeSwitch == null) {
            if (other.volumeSwitch != null) return false;
        } else if (!volumeSwitch.equals(other.volumeSwitch)) return false;
        return true;
    }

    public int getAlbum() {
        return album;
    }

    public Mp3PlayerMode getMode() {
        return mode;
    }

    public Switch getPlayPauseSwitch() {
        return playPauseSwitch;
    }

    public int getTrack() {
        return track;
    }

    public int getVolume() {
        return volume;
    }

    public Switch getVolumeLeftSwitch() {
        return volumeLeftSwitch;
    }

    public Switch getVolumeRightSwitch() {
        return volumeRightSwitch;
    }

    public Switch getVolumeSwitch() {
        return volumeSwitch;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + album;
        result = prime * result + (mode == null ? 0 : mode.hashCode());
        result = prime * result + (playPauseSwitch == null ? 0 : playPauseSwitch.hashCode());
        result = prime * result + track;
        result = prime * result + volume;
        result = prime * result + (volumeLeftSwitch == null ? 0 : volumeLeftSwitch.hashCode());
        result = prime * result + (volumeRightSwitch == null ? 0 : volumeRightSwitch.hashCode());
        result = prime * result + (volumeSwitch == null ? 0 : volumeSwitch.hashCode());
        return result;
    }

    public void setAlbum(final int album) {
        this.album = album;
    }

    public void setMode(final Mp3PlayerMode mode) {
        this.mode = mode;
    }

    public void setPlayPauseSwitch(final Switch playPauseSwitch) {
        this.playPauseSwitch = playPauseSwitch;
    }

    public void setTrack(final int track) {
        this.track = track;
    }

    public void setVolume(final int volume) {
        this.volume = volume;
    }

    public void setVolumeLeftSwitch(final Switch volumeLeftSwitch) {
        this.volumeLeftSwitch = volumeLeftSwitch;
    }

    public void setVolumeRightSwitch(final Switch volumeRightSwitch) {
        this.volumeRightSwitch = volumeRightSwitch;
    }

    public void setVolumeSwitch(final Switch volumeSwitch) {
        this.volumeSwitch = volumeSwitch;
    }
}
