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
package de.treichels.hott.model.serial

import java.io.Serializable
import java.util.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
class FileInfo : Serializable, Comparable<String> {
    val dir: String
    val name: String
    val shortName: String
    val size: Int
    val modifyDate: Date
    val type: FileType

    val path: String
        get() = dir + (if (dir.endsWith(FORWARD_SLASH)) EMPTY_STRING else FORWARD_SLASH) + name

    constructor(path: String, shortName: String, size: Int, modifyDate: Date, type: FileType) {
        val pos = path.lastIndexOf(FORWARD_SLASH)
        if (pos == -1) {
            dir = FORWARD_SLASH
            name = path
            this.shortName = shortName
            this.size = size
            this.modifyDate = modifyDate
            this.type = type
        } else {
            dir = path.substring(0, pos)
            name = path.substring(pos + 1)
            this.shortName = shortName
            this.size = size
            this.modifyDate = modifyDate
            this.type = type
        }
    }

    /**
     * @param name
     * @param shortName
     * @param size
     * @param modifyDate
     * @param type
     */
    constructor(dir: String, name: String, shortName: String, size: Int, modifyDate: Date, type: FileType) {
        this.dir = dir
        this.name = name
        this.shortName = shortName
        this.size = size
        this.modifyDate = modifyDate
        this.type = type
    }

    override fun compareTo(other: String): Int {
        return name.compareTo(other)
    }


    override fun toString(): String {
        return String.format("FileInfo [portName=%s, shortName=%s, size=%s, modifyDate=%s, type=%s]", name, shortName, size, modifyDate, type) //$NON-NLS-1$
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FileInfo) return false

        if (dir != other.dir) return false
        if (name != other.name) return false
        if (shortName != other.shortName) return false
        if (size != other.size) return false
        if (modifyDate != other.modifyDate) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dir.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + shortName.hashCode()
        result = 31 * result + size
        result = 31 * result + modifyDate.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    companion object {
        private val EMPTY_STRING = "" //$NON-NLS-1$
        private val FORWARD_SLASH = "/" //$NON-NLS-1$
        private const val serialVersionUID = 1L
    }
}
