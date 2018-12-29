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
package de.treichels.hott.mdlviewer.javafx

import de.treichels.hott.util.Util
import tornadofx.*

class MdlViewer : App() {
    companion object {
        val version: String by lazy {
            Util.sourceVersion(MdlViewer::class)
        }

        val programDir: String by lazy {
            Util.programDir(MdlViewer::class)
        }
    }

    init {
        Util.enableLogging()
    }

    override val primaryView = MainView::class
}
