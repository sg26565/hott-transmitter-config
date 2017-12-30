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
package de.treichels.hott.model.enums

import tornadofx.*
import java.util.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
enum class TransmitterType(val productCode: Int) {
    mc16(16004600), mc20(16004300), mc26(16007700), mc28(16007100), mc32(16004100), mx12(16003600), mx16(16003300), mx20(16003700), mz12(16005100),
    mz12Pro(16007800), mz18(16005300), mz24(16005200), mz24Pro(16007200), x8n(16006200), x8e(16006500), unknown(0);

override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]

    companion object {
        fun forProductCode(productCode: Int): TransmitterType {
            return TransmitterType.values().firstOrNull { productCode == it.productCode } ?: unknown
        }
    }
}
