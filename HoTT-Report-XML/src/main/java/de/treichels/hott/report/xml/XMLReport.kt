/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.treichels.hott.report.xml

import de.treichels.hott.model.BaseModel
import de.treichels.hott.model.CurveMixer
import de.treichels.hott.model.LinearMixer
import de.treichels.hott.model.HelicopterModel
import de.treichels.hott.model.HelicopterPhase
import de.treichels.hott.model.WingedModel
import de.treichels.hott.model.WingedPhase
import java.io.ByteArrayOutputStream
import java.io.File
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller
import javax.xml.bind.SchemaOutputResolver
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

    fun generateXsd(file: File) = CTX.generateSchema(object : SchemaOutputResolver() {
        override fun createOutput(namespaceUri: String?, suggestedFileName: String?): Result {
            return StreamResult(file)
        }
    })
}
