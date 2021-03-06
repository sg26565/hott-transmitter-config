/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.treichels.hott.report.xml

import de.treichels.hott.model.*
import java.io.ByteArrayOutputStream
import java.io.File
import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.JAXBException
import jakarta.xml.bind.Marshaller
import jakarta.xml.bind.SchemaOutputResolver
import javax.xml.transform.Result
import javax.xml.transform.stream.StreamResult

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
object XMLReport {
    // TODO: add other model types
    private val CTX: JAXBContext = JAXBContext.newInstance(WingedModel::class.java, WingedPhase::class.java,
            HelicopterModel::class.java, HelicopterPhase::class.java, LinearMixer::class.java, CurveMixer::class.java)
    private val MARSHALLER: Marshaller = CTX.createMarshaller()

    init {
        // setup JAXB
        MARSHALLER.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, java.lang.Boolean.TRUE)
    }

    @Throws(JAXBException::class)
    fun generateXML(model: BaseModel) = ByteArrayOutputStream().apply { MARSHALLER.marshal(model, this) }.toString()

    @Suppress("unused")
    fun generateXsd(file: File) = CTX.generateSchema(object : SchemaOutputResolver() {
        override fun createOutput(namespaceUri: String?, suggestedFileName: String?): Result {
            return StreamResult(file)
        }
    })
}
