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
package gde.model.enums;

import java.util.ResourceBundle;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public enum TransmitterType {
    mc16(16004600), mc20(16004300), mc26(16007700), mc28(16007100), mc32(16004100), mx12(16003600), mx16(16003300), mx20(16003700), mz12(-1), mz18(-2),
    mz24(16005200), mz24Pro(16007200), unknown(0);

    public static TransmitterType forProductCode(final int productCode) {
        for (final TransmitterType t : TransmitterType.values())
            if (productCode == t.productCode) return t;

        return unknown;
    }

    private final int productCode;

    private TransmitterType(final int productCode) {
        this.productCode = productCode;
    }

    public int getProductCode() {
        return productCode;
    }

    /** @return the locale-dependent message */
    @Override
    public String toString() {
        return ResourceBundle.getBundle(getClass().getName()).getString(name());
    }
}
