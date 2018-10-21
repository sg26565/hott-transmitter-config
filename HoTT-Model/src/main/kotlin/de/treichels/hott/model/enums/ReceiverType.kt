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

import de.treichels.hott.model.firmware.Firmware
import de.treichels.hott.model.firmware.Updatable
import tornadofx.*
import java.util.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
enum class ReceiverType(override val productCode: Int = 0, val orderNo: String = "", val id: Int = 0, val hasGyro: Boolean = false, val hasVario: Boolean = false) : Updatable<ReceiverType> {
    gr4(16005600, "33502"),
    gr8(16005700, "33504"),
    gr10c(0, "S1029", 0x3d, true),
    gr12(16003500, "33506"),
    gr12l(16003510, "S1012"),
    gr12l_sumd(16003540, "S1037"),
    gr12sc(0, "33566", 0x91),
    gr12sh(0, "33565", 0x91),
    gr12sh_3xg(16006000, "33575", 0x75, true),
    gr12_3xg(16005400, "33576", 0x76, true),
    gr12_3xg_vario(16005500, "33577", 0x77, true, true),
    gr12s(16003400, "33505"),
    gr16(16003100, "33508"),
    gr16l(16003112, "S1021"),
    gr18(16006100, "33579", 0x79, true, true),
    gr18c(16006160, "S1019", 0x19, true),
    gr18_alpha(19000710, hasGyro = true, hasVario = true),
    gr18c_alpha(19001000, hasVario = true),
    gr24(16003200, "33512"),
    gr24l(16003201, "S1022"),
    gr24pro(16005800, "33583", 0x97, true, true),
    gr32(16004000, "33516"),
    gr32l(16004020, "S1023"),
    falcon12(0, "S1035", 0x35, true);

    override fun getFirmware(): List<Firmware<ReceiverType>> {
        var result = emptyList<Firmware<ReceiverType>>()

        if (productCode != 0) {
            result += Firmware.list(this, "HoTT_Receiver")
            result += Firmware.list(this, "HoTT_Receiver", (productCode % 10000).toString())
        }

        if (id != 0) {
            result += Firmware.list(this, "Server Updates", Integer.toHexString(id).toUpperCase())
        }

        return result
    }

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]

    companion object {
        fun forProductCode(productCode: Int): ReceiverType? {
            return values().firstOrNull { s -> s.productCode == productCode || s.productCode % 10000 == productCode }
        }

        fun forId(id: Int): ReceiverType? {
            return values().firstOrNull { s -> s.id == id }
        }

        fun forOrderNo(orderNo: String): ReceiverType? {
            return values().firstOrNull { s -> s.orderNo == orderNo }
        }
    }
}
