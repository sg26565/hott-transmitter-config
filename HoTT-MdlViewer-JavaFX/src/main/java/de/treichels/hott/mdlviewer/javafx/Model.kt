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

import de.treichels.hott.decoder.HoTTSerialPort
import de.treichels.hott.model.BaseModel
import de.treichels.hott.model.HoTTException
import de.treichels.hott.model.enums.ModelType
import de.treichels.hott.model.serial.FileInfo
import de.treichels.hott.model.serial.ModelInfo
import de.treichels.hott.report.html.HTMLReport
import de.treichels.hott.report.pdf.PDFReport
import de.treichels.hott.report.xml.XMLReport
import de.treichels.hott.util.Util
import tornadofx.*
import tornadofx.FX.Companion.messages
import java.io.*


class Model(private val info: ModelInfo, private val data: ByteArray?) {
    companion object {
        fun loadModel(file: File?): Model {
            // check if file exits
            if (file == null || !file.exists()) throw HoTTException(messages["file_not_found"], file)

            // check that file has a .mdl extension
            val fileName = file.name
            if (!fileName.endsWith(".mdl")) throw HoTTException(messages["invalid_file_name"], fileName)

            // check model type
            val modelType = ModelType.forChar(fileName[0])
            val modelName = fileName.substring(1, fileName.length - 4)
            val info = ModelInfo(modelNumber = 0, modelName = modelName, modelType = modelType, receiverType = null, transmitterType = null)
            val data = ByteArray(file.length().toInt())
            FileInputStream(file).use { it.read(data) }

            return Model(info, data)
        }

        fun loadModel(fileInfo: FileInfo, serialPort: HoTTSerialPort? = null): Model {
            val fileName = fileInfo.name
            val type = ModelType.forChar(fileName[0])
            val name = fileName.substring(1, fileName.length - 4)
            val modelInfo = ModelInfo(modelNumber = 0, modelName = name, modelType = type, receiverType = null, transmitterType = null)
            val data = serialPort?.use { p ->
                p.open()
                ByteArrayOutputStream().use { stream ->
                    p.readFile(fileInfo.path, stream)
                    stream.toByteArray()
                }
            }

            return Model(modelInfo, data)
        }
    }

    val fileName: String = "${info.modelType.char}${info.modelName}"
    val model: BaseModel by lazy { de.treichels.hott.decoder.HoTTDecoder.decodeStream(info.modelType, info.modelName, ByteArrayInputStream(data)) }
    val html: String by lazy { HTMLReport.generateHTML(model) }
    @Suppress("MemberVisibilityCanPrivate")
    val xml: String by lazy { XMLReport.generateXML(model) }

    fun saveHtml(file: File) = HTMLReport.save(file, html)
    fun saveMdl(file: File) = FileOutputStream(file).use { it.write(data) }
    fun savePdf(file: File) = PDFReport.save(file, html)
    fun saveTxt(file: File) = FileOutputStream(file).use { it.write(Util.dumpData(data).toByteArray()) }
    fun saveXml(file: File) = HTMLReport.save(file, xml)
}
