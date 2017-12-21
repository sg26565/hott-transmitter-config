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

import de.treichels.decoder.HoTTDecoder
import de.treichels.hott.model.BaseModel
import de.treichels.hott.model.HoTTException
import de.treichels.hott.model.enums.ModelType
import de.treichels.hott.model.serial.ModelInfo
import de.treichels.hott.report.html.HTMLReport
import de.treichels.hott.report.pdf.PDFReport
import de.treichels.hott.report.xml.XMLReport
import de.treichels.hott.util.Util
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun loadModel(file: File?): Model {
    // check if file exits
    if (file == null || !file.exists()) throw HoTTException("FileNotFound", file) //$NON-NLS-1$

    // check that file has a .mdl extension
    val fileName = file.name
    if (!fileName.endsWith(".mdl")) throw HoTTException("InvalidFileName", fileName) //$NON-NLS-1$

    // check model type
    val modelType = ModelType.forChar(fileName[0])
    val modelName = fileName.substring(1, fileName.length - 4)
    val info = ModelInfo(0, modelName, modelType, null, null)
    val data = ByteArray(file.length().toInt())
    FileInputStream(file).use { it.read(data) }

    return Model(info, data)
}

class Model(private val info: ModelInfo, private val data: ByteArray?) {
    val fileName: String = "${info.modelType.char}${info.modelName}"
    val model: BaseModel by lazy { HoTTDecoder.decodeStream(info.modelType, info.modelName, ByteArrayInputStream(data)) }
    val html: String by lazy { HTMLReport.generateHTML(model) }
    @Suppress("MemberVisibilityCanPrivate")
    val xml: String by lazy { XMLReport.generateXML(model) }

    fun saveHtml(file: File) = HTMLReport.save(file, html)
    fun saveMdl(file: File) = FileOutputStream(file).use { it.write(data) }
    fun savePdf(file: File) = PDFReport.save(file, html)
    fun saveTxt(file: File) = FileOutputStream(file).use { it.write(Util.dumpData(data).toByteArray()) }
    fun saveXml(file: File) = HTMLReport.save(file, xml)
}
