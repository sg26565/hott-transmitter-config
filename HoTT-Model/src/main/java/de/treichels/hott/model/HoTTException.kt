/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package de.treichels.hott.model

import de.treichels.hott.messages.Messages
import java.io.IOException

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
open class HoTTException @JvmOverloads constructor(format: String? = null, cause: Throwable? = null, val args: Array<out Any?>? = null) : IOException(format, cause) {
    constructor(cause: Throwable) : this(format = null, cause = cause, args = null)
    constructor(message: String, vararg args: Any?) : this(format = message, cause = null, args = args)

    override val message: String?
        get() = Messages.getString(super.message, *args ?: emptyArray())
}