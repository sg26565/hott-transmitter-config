/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.report.html


import de.treichels.hott.model.Curve

/**
 * An interface that generates an inline representation of a PNG image for a [Curve] object. Implementation should draw an in-memory image for the
 * [Curve] object, convert it into PNG format and compress the data as a base64 encoded string.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
interface CurveImageGenerator {
    companion object {
        const val PREFIX = "data:image/png;base64,"
    }

    fun getImageSource(curve: Curve, scale: Double, description: Boolean): String
}
