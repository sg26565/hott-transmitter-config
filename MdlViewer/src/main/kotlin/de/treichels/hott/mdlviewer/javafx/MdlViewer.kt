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
import tornadofx.FX.Companion.messages
import java.io.File
import java.util.jar.JarFile
import java.util.logging.LogManager

class MdlViewer : App() {
    companion object {
        private val sourceLocation = File(MdlViewer::class.java.protectionDomain.codeSource.location.toURI())

        val version: String by lazy {
            var result = messages["Unknown"]

            if (sourceLocation.name.endsWith(".jar") || sourceLocation.name.endsWith(".exe"))
                JarFile(sourceLocation).use { jarFile ->
                    val attributes = jarFile.manifest.mainAttributes
                    result = "v${attributes.getValue("Implementation-Version")}.${attributes.getValue("Implementation-Build")}"
                }

            result
        }

        val programDir: String by lazy {
            // get the parent directory containing the jar file or the classes directory
            val programDir = sourceLocation.parentFile

            // if we are running inside Eclipse in the target directory, step up to the project level
            if (programDir.name == "target") programDir.parentFile.absolutePath else programDir.absolutePath
        }

    }

    init {
        // setup logging
        if (Util.DEBUG) LogManager.getLogManager().readConfiguration(ClassLoader.getSystemResourceAsStream("logging.properties"))
    }

    override val primaryView = MainView::class
}
