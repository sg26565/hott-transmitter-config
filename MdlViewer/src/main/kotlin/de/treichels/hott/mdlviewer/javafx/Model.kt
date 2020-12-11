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

import de.treichels.hott.decoder.HoTTDecoderKt.decodeStream
import de.treichels.hott.decoder.HoTTTransmitter
import de.treichels.hott.model.BaseModel
import de.treichels.hott.model.HoTTException
import de.treichels.hott.model.enums.ModelType
import de.treichels.hott.model.enums.ReceiverClass
import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.report.html.HTMLReport
import de.treichels.hott.report.pdf.PDFReport
import de.treichels.hott.report.xml.XMLReport
import de.treichels.hott.serial.FileInfo
import de.treichels.hott.serial.ModelInfo
import de.treichels.hott.util.Util
import tornadofx.*
import tornadofx.FX.Companion.messages
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream


class Model(private val info: ModelInfo, private val data: ByteArray) {
    companion object {
        fun loadModel(file: File): Model {
            // check if file exits
            if (!file.exists()) throw HoTTException(messages["file_not_found"], file)

            // check that file has a .mdl extension
            val fileName = file.name
            if (!fileName.endsWith(".mdl")) throw HoTTException(messages["invalid_file_name"], fileName)

            // check model type
            val modelType = ModelType.forChar(fileName[0])
            val modelName = fileName.substring(1, fileName.length - 4)
            val info = ModelInfo(modelNumber = 0, modelName = modelName, modelInfo = "", modelType = modelType, receiverClass = ReceiverClass.Unknown, transmitterType = TransmitterType.Unknown)
            val data = ByteArray(file.length().toInt())
            FileInputStream(file).use { it.read(data) }

            return Model(info, data)
        }

        fun loadModel(fileInfo: FileInfo, transmitter: HoTTTransmitter): Model {
            val fileName = fileInfo.name
            val type = ModelType.forChar(fileName[0])
            val name = fileName.substring(1, fileName.length - 4)
            val modelInfo = ModelInfo(modelNumber = 0, modelName = name, modelInfo = "", modelType = type, receiverClass = ReceiverClass.Unknown, transmitterType = TransmitterType.Unknown)
            val data = ByteArrayOutputStream().use { stream ->
                transmitter.readFile(fileInfo.path, stream)
                stream.toByteArray()
            }

            return Model(modelInfo, data)
        }

        fun loadModel(modelInfo: ModelInfo, transmitter: HoTTTransmitter) = Model(modelInfo, transmitter.getModelData(modelInfo))
    }

    val fileName: String = "${info.modelType.char}${info.modelName}"
    val model: BaseModel by lazy { decodeStream(info.modelType, info.modelName, ByteArrayInputStream(data)) }
    val html
        get() = HTMLReport.generateHTML(model)
    @Suppress("MemberVisibilityCanBePrivate")
    val xml
        get() = XMLReport.generateXML(model)

    fun saveHtml(file: File) = file.writeText(html)
    fun saveMdl(file: File) = file.writeBytes(data)
    fun savePdf(file: File) = PDFReport.save(file, html)
    fun saveTxt(file: File) = file.writeText(Util.dumpData(data))
    fun saveXml(file: File) = file.writeText(xml)
}
