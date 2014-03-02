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

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author oli@treichels.de
 * 
 */
public final class TxInfo implements Serializable {
  private static final long     serialVersionUID = 1L;

  private final TransmitterType transmitterType;
  private final int             appVersion;
  private final int             memoryVersion;
  private final int             year;
  private final String          transmitterName;
  private final String          vendorName;
  private final String          ownerName;
  private final String[]        customPhaseNames;
  private final long            rfId;

  /**
   * @param transmitterType
   * @param appVersion
   * @param memoryVersion
   * @param year
   * @param transmitterName
   * @param vendorName
   */
  public TxInfo(final Long rfId, final TransmitterType transmitterType, final int appVersion, final int memoryVersion, final int year,
      final String transmitterName, final String vendorName, final String ownerName, final String[] customPhaseNames) {
    this.rfId = rfId;
    this.transmitterType = transmitterType;
    this.appVersion = appVersion;
    this.memoryVersion = memoryVersion;
    this.year = year;
    this.transmitterName = transmitterName;
    this.vendorName = vendorName;
    this.ownerName = ownerName;
    this.customPhaseNames = customPhaseNames;
  }

  public int getAppVersion() {
    return appVersion;
  }

  public String[] getCustomPhaseNames() {
    return customPhaseNames;
  }

  public int getMemoryVersion() {
    return memoryVersion;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public long getRfId() {
    return rfId;
  }

  public String getTransmitterName() {
    return transmitterName;
  }

  public TransmitterType getTransmitterType() {
    return transmitterType;
  }

  public String getVendorName() {
    return vendorName;
  }

  public int getYear() {
    return year;
  }

  @Override
  public String toString() {
    return String.format(
        "TxInfo [transmitterType=%s, appVersion=%s, memoryVersion=%s, year=%s, transmitterName=%s, vendorName=%s, ownerName=%s, customPhaseNames=%s]",
        transmitterType, appVersion, memoryVersion, year, transmitterName, vendorName, ownerName, Arrays.toString(customPhaseNames));
  }
}
