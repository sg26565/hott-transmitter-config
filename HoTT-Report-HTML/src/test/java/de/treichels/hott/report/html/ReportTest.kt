/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.report.html

import de.treichels.hott.decoder.HoTTDecoder
import de.treichels.hott.model.enums.TransmitterType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.io.File
import java.net.URL

class ReportTest {
    private val path = javaClass.`package`.name.replace(".", "/") + "/models/"
    private val filter = { file: File -> file.name.endsWith(".mdl") }

    @Test
    fun testAllModels() {
        TransmitterType.values().forEach { transmitterType ->
            val url: URL? = ClassLoader.getSystemResource(path + transmitterType.name)

            if (url != null)
                File(url.toURI()).listFiles(filter)?.forEach { file ->
                    val model = HoTTDecoder.decodeFile(file)
                    assertEquals(transmitterType, model.transmitterType)

                    val html = HTMLReport.generateHTML(model)
                    assertFalse(html.isEmpty())
                }
        }
    }
}