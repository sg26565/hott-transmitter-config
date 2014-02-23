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

import java.util.Date;

/**
 * @author oli@treichels.de
 */
public class FileInfo {
  private final String   name;
  private final String   shortName;
  private final int      size;
  private final Date     modifyDate;
  private final FileType type;

  /**
   * @param name
   * @param shortName
   * @param size
   * @param modifyDate
   * @param type
   */
  public FileInfo(final String name, final String shortName, final int size, final Date modifyDate, final FileType type) {
    this.name = name;
    this.shortName = shortName;
    this.size = size;
    this.modifyDate = modifyDate;
    this.type = type;
  }

  public Date getModifyDate() {
    return modifyDate;
  }

  public String getName() {
    return name;
  }

  public String getShortName() {
    return shortName;
  }

  public int getSize() {
    return size;
  }

  public FileType getType() {
    return type;
  }

  @Override
  public String toString() {
    return String.format("FileInfo [name=%s, shortName=%s, size=%s, modifyDate=%s, type=%s]", name, shortName, size, modifyDate, type);
  }
}
