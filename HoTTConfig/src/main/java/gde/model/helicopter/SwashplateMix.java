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
package gde.model.helicopter;

/**
 * @author oli@treichels.de
 */
public class SwashplateMix {
  private int nickMix;
  private int pitchMix;
  private int rollMix;

  public int getNickMix() {
    return nickMix;
  }

  public int getPitchMix() {
    return pitchMix;
  }

  public int getRollMix() {
    return rollMix;
  }

  public void setNickMix(final int nickMix) {
    this.nickMix = nickMix;
  }

  public void setPitchMix(final int pitchMix) {
    this.pitchMix = pitchMix;
  }

  public void setRollMix(final int rollMix) {
    this.rollMix = rollMix;
  }
}
