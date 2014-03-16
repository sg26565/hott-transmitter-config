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

import java.io.IOException;

/**
 * @author oli@treichels.de
 */
public class HoTTException extends IOException {
  private static final long serialVersionUID = 1L;
  private String            format           = null;
  private Object[]          args;

  public HoTTException() {
    super();
  }

  public HoTTException(final String message) {
    super(message);
  }

  public HoTTException(final String format, final Object... args) {
    super(String.format(format, args));
    this.format = format;
    this.args = args;
  }

  public HoTTException(final Throwable cause) {
    super(cause);
  }

  public HoTTException(final Throwable cause, final String format, final Object... args) {
    super(String.format(format, args), cause);
    this.format = format;
    this.args = args;
  }

  @Override
  public String getLocalizedMessage() {
    if (format != null) {
      final String localizedFormat = localizeString(format);
      return String.format(localizedFormat, args);
    } else {
      return localizeString(getMessage());
    }
  }

  private String localizeString(final String text) {
    return text; // TODO
  }
}