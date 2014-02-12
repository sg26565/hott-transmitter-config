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
package gde.model.serial;

import gde.model.enums.TransmitterType;

/**
 * @author oli@treichels.de
 * 
 */
public final class TxInfo {
  private final TransmitterType transmitterType;
  private final int             appVersion;
  private final int             memoryVersion;
  private final int             year;
  private final String          name;
  private final String          vendor;

  /**
   * @param transmitterType
   * @param appVersion
   * @param memoryVersion
   * @param year
   * @param name
   * @param vendor
   */
  public TxInfo(final TransmitterType transmitterType, final int appVersion, final int memoryVersion, final int year, final String name, final String vendor) {
    super();
    this.transmitterType = transmitterType;
    this.appVersion = appVersion;
    this.memoryVersion = memoryVersion;
    this.year = year;
    this.name = name;
    this.vendor = vendor;
  }

  public int getAppVersion() {
    return appVersion;
  }

  public int getMemoryVersion() {
    return memoryVersion;
  }

  public String getName() {
    return name;
  }

  public TransmitterType getTransmitterType() {
    return transmitterType;
  }

  public String getVendor() {
    return vendor;
  }

  public int getYear() {
    return year;
  }
}
