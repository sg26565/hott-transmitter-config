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
package gde.report;

import gde.model.Curve;

/**
 * An interface that generates an inline representation of a PNG image for a {@link Curve} object. Implementation should draw an in-memory image for the
 * {@link Curve} object, convert it into PNG format and compress the data as a base64 encoded string.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public interface CurveImageGenerator {
    public static final String PREFIX = "data:image/png;base64,"; //$NON-NLS-1$

    public abstract String getImageSource(final Curve curve, final float scale, final boolean description);
}