/**
 *  HoTT Transmitter Config
 *  Copyright (C) 2013  Oliver Treichel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package gde.model;

import gde.model.enums.Mp3PlayerMode;

/**
 * @author oli
 */
public class Mp3Player {
  private int           album;
  private Mp3PlayerMode mode;
  private int           track;
  private int           volume;

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Mp3Player other = (Mp3Player) obj;
    if (album != other.album) {
      return false;
    }
    if (mode != other.mode) {
      return false;
    }
    if (track != other.track) {
      return false;
    }
    if (volume != other.volume) {
      return false;
    }
    return true;
  }

  public int getAlbum() {
    return album;
  }

  public Mp3PlayerMode getMode() {
    return mode;
  }

  public int getTrack() {
    return track;
  }

  public int getVolume() {
    return volume;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + album;
    result = prime * result + (mode == null ? 0 : mode.hashCode());
    result = prime * result + track;
    result = prime * result + volume;
    return result;
  }

  public void setAlbum(final int album) {
    this.album = album;
  }

  public void setMode(final Mp3PlayerMode mode) {
    this.mode = mode;
  }

  public void setTrack(final int track) {
    this.track = track;
  }

  public void setVolume(final int volume) {
    this.volume = volume;
  }
}
