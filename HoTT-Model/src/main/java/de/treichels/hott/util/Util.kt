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
package de.treichels.hott.util

import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */

object Util {
    private const val PARAM_OFFLINE = "offline"
    private const val PARAM_DEBUG = "debug"
    private const val LATEST_VERSIONS_URL = "https://drive.google.com/uc?export=download&id=0B_uPguA0xiT4SUl1V1VKYXFjWHc"
    private val latestVersions = Properties()
    val OFFLINE = java.lang.Boolean.getBoolean(PARAM_OFFLINE)
    val DEBUG = java.lang.Boolean.getBoolean(PARAM_DEBUG)

    @JvmOverloads
    fun dumpData(data: ByteArray?, baseAddress: Int = 0): String {
        val sb = StringBuilder()

        if (data != null) {
            val len = data.size
            var addr = 0

            while (addr < len) {
                sb.append(String.format("0x%04x: ", baseAddress + addr)) //$NON-NLS-1$

                for (i in 0..15)
                    if (addr + i < len) {
                        when (i) {
                            4, 12 -> sb.append(':')

                            0, 8 -> sb.append('|')

                            else -> sb.append(' ')
                        }

                        sb.append(String.format("%02x", data[addr + i])) //$NON-NLS-1$
                    } else
                        sb.append("   ") //$NON-NLS-1$

                sb.append("| ") //$NON-NLS-1$

                (0..15).asSequence()
                        .filter { addr + it < len }
                        .map { (data[addr + it].toInt() and 0xff).toChar() }
                        .forEach {
                            if (it.toInt() in 0x20..0x7e)
                                sb.append(it)
                            else
                                sb.append('.')
                        }

                sb.append('\n')
                addr += 16
            }
        }

        return sb.toString()
    }

    fun getLatestVersion(key: String): String? {
        if (latestVersions.isEmpty)
            try {
                URL(LATEST_VERSIONS_URL).openStream().use { `is` ->
                    InputStreamReader(`is`).use { reader ->
                        latestVersions.load(reader)
                        latestVersions.setProperty(PARAM_OFFLINE, java.lang.Boolean.FALSE.toString())
                    }
                }
            } catch (e: IOException) {
                latestVersions.setProperty(PARAM_OFFLINE, java.lang.Boolean.TRUE.toString())
            }

        return latestVersions.getProperty(key, null)
    }
}
