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
package gde.model.winged;

/**
 * @author oli@treichels.de
 */
public class WingedTrim {
  private int[] aileronPhaseTrim;
  private int   aileronStickTrim;
  private int   elevatorPhaseTrim;
  private int   elevatorStickTrim;
  private int[] flapPhaseTrim;
  private int   rudderStickTrim;

  public int[] getAileronPhaseTrim() {
    return aileronPhaseTrim;
  }

  public int getAileronStickTrim() {
    return aileronStickTrim;
  }

  public int getElevatorPhaseTrim() {
    return elevatorPhaseTrim;
  }

  public int getElevatorStickTrim() {
    return elevatorStickTrim;
  }

  public int[] getFlapPhaseTrim() {
    return flapPhaseTrim;
  }

  public int getRudderStickTrim() {
    return rudderStickTrim;
  }

  public void setAileronPhaseTrim(final int[] aileronPhaseTrim) {
    this.aileronPhaseTrim = aileronPhaseTrim;
  }

  public void setAileronStickTrim(final int aileronStickTrim) {
    this.aileronStickTrim = aileronStickTrim;
  }

  public void setElevatorPhaseTrim(final int elevatorPhaseTrim) {
    this.elevatorPhaseTrim = elevatorPhaseTrim;
  }

  public void setElevatorStickTrim(final int elevtorStickTrim) {
    elevatorStickTrim = elevtorStickTrim;
  }

  public void setFlapPhaseTrim(final int[] flapPhaseTrim) {
    this.flapPhaseTrim = flapPhaseTrim;
  }

  public void setRudderStickTrim(final int rudderStickTrim) {
    this.rudderStickTrim = rudderStickTrim;
  }
}
