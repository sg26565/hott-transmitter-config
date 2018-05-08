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

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.math.BigInteger
import java.net.URL
import java.security.MessageDigest
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

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
                sb.append(String.format("0x%04x: ", baseAddress + addr))

                for (i in 0..15)
                    if (addr + i < len) {
                        when (i) {
                            4, 12 -> sb.append(':')

                            0, 8 -> sb.append('|')

                            else -> sb.append(' ')
                        }

                        sb.append(String.format("%02x", data[addr + i]))
                    } else
                        sb.append("   ")

                sb.append("| ")

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

const val HASH_ALGORITHM = "MD5"
const val HASH_SIZE = 32 // 128 bit => 16 byte => 32 hex characters
private const val BUFFER_SIZE = 1024 * 1024

// convert message digest into zero padded hex string
fun MessageDigest.getHash() = BigInteger(1, digest()).toString(16).padStart(HASH_SIZE, '0')

// compute hash for a zip entry
fun ZipFile.hash(entry: ZipEntry) = getInputStream(entry).hash()

// compute hash for a regular file
fun File.hash() = inputStream().hash()

// compute hash for a byte array
fun ByteArray.hash() = inputStream().hash()

// compute hash for an input stream. The stream will be read to the end and closed.
fun InputStream.hash(): String {
    val md = MessageDigest.getInstance(HASH_ALGORITHM)
    val buffer = ByteArray(BUFFER_SIZE)

    use { stream ->
        while (true) {
            val len = stream.read(buffer)
            if (len >= 0) md.update(buffer, 0, len) else break
        }
    }

    return md.getHash()
}

// extract a zip entry to a file and return its hash
fun ZipFile.extract(zipEntry: ZipEntry, file: File): String {
    val md = MessageDigest.getInstance(HASH_ALGORITHM)
    val buffer = ByteArray(BUFFER_SIZE)

    file.parentFile.mkdirs()

    getInputStream(zipEntry).use { inputStream ->
        file.outputStream().use { outputStream ->
            while (true) {
                val len = inputStream.read(buffer)
                if (len >= 0) {
                    md.update(buffer, 0, len)
                    outputStream.write(buffer, 0, len)
                } else break
            }
        }
    }

    file.setLastModified(zipEntry.time)

    return md.getHash()
}

