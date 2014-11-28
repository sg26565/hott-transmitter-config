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
package de.treichels.hott.android.background.serial;

/**
 * A data holder class that hold information about communication devices (i.e. USB or Bluetooth) on the mobile device.
 *
 * @author oli
 */
public class DeviceInfo<DeviceType> {
  private DeviceType device;
  private long       id;
  private String     name;

  public DeviceInfo() {}

  public DeviceType getDevice() {
    return device;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setDevice(final DeviceType device) {
    this.device = device;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public void setName(final String name) {
    this.name = name;
  }
}