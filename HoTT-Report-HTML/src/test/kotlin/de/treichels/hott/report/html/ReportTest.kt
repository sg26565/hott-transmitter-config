/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.report.html

import de.treichels.hott.decoder.HoTTDecoderKt.decodeFile
import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.util.Util.dumpData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.io.File
import java.net.URL

class ReportTest {
    private val path = javaClass.`package`.name.replace(".", "/") + "/models/"

    @Test
    fun testAllModels() {
        TransmitterType.values().forEach { transmitterType ->
            val url: URL? = ClassLoader.getSystemResource(path + transmitterType.name)

            if (url != null) {
                val dir =File(url.toURI())

                dir.listFiles { file -> file.name.endsWith(".mdl") }?.forEach { mdlFile ->
                    val baseName = mdlFile.name.substringBefore(".mdl")

                    println(baseName)

                    val dump = dumpData(mdlFile.readBytes())
                    val txtFile = File(dir, "$baseName.txt")

                    txtFile.writeText(dump)
                    println (txtFile.absolutePath)
//                    assertTrue( txtFile.exists() )
//                    assertTrue( txtFile.canRead() )
//                    assertEquals(txtFile.readText(), dump)

                    try {
                        val model = decodeFile(mdlFile)
                        assertEquals(transmitterType, model.transmitterType)

                        val html = HTMLReport.generateHTML(model)
                        assertFalse(html.isEmpty())

                        val htmlFile = File(dir, "$baseName.2.html")
                        htmlFile.writeText(html)
                        println(htmlFile.absolutePath)
//                    assertTrue( htmlFile.exists() )
//                    assertTrue( htmlFile.canRead() )
//                    assertEquals(htmlFile.readText(), html)
                    } catch (e:NotImplementedError) {
                        // ignore
                    }
                }
            }
        }
    }
}
