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

import de.treichels.hott.util.get
import java.util.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
enum class TransmitterType(override val productCode: Int = 0, override val orderNo: String = "", override val category: String = TransmitterType.category, override val baudRate: Int = 115200) : Registered<TransmitterType> {
    mc16(16004600, "33016"), mc20(16004300, "33020"), mc26(16007700, "S1036"), mc28(16007100, "33028"),
    mc32(16004100, "33032"), mc32Expert(16008500), mc32ExpertBeta(16008501),
    mxs8(16004900,"33200"), mx10(16004200, "33110"), mx12(16003600, "33112"), mx16(16003300, "33116"),
    mx20(16003700,"33124"), mz10(16005000, "S1042"), mz12(16005100, "S1002"), mz12Pro(16007800, "S1002.PRO"),
    mz16(16008600), mz16beta(16008601), mz18(16005300,"S1005"), mz18Pro(16008300, "S1005.PRO"),
    mz24(16005200, "S1006"), mz24Pro(16007200, "S1006.PRO"), mz32(16008200, "S1024"), mz32beta(16008201, "S1024"),
    x4s(16005900, "33400"), x8n(16006200, "S1018"), x8e(16006500, "S1008"), Unknown;

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name] + if (orderNo.isNotEmpty()) " ($orderNo)" else ""

    companion object {
        fun forProductCode(productCode: Int): TransmitterType = TransmitterType.values().firstOrNull { productCode == it.productCode }
                ?: Unknown

        const val category = "Transmitter"
    }
}
