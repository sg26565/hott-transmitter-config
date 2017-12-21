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

import java.io.IOException;

import de.treichels.hott.messages.Messages;
import de.treichels.hott.util.Util;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class HoTTException extends IOException {
    private static final long serialVersionUID = 1L;
    private Object[] args;

    public HoTTException() {
        super();
    }

    public HoTTException(final String message, final Object... args) {
        this(message, null, args);
    }

    public HoTTException(final String format, final Throwable cause, final Object... args) {
        super(format, cause);
        this.args = args;
        if (Util.DEBUG) printStackTrace();
    }

    public HoTTException(final Throwable cause) {
        this(null, cause);
    }

    @Override
    public String getMessage() {
        return Messages.getString(super.getMessage(), args);
    }
}