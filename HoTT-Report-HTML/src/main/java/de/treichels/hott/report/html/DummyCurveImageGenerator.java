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
package de.treichels.hott.report.html;

import de.treichels.hott.model.Curve;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public class DummyCurveImageGenerator implements CurveImageGenerator {
    @Override
    public String getImageSource(final Curve curve, final float scale, final boolean description) {
        return ""; //$NON-NLS-1$
    }
}
