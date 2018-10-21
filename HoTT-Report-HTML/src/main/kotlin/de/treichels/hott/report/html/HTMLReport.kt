/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.treichels.hott.report.html

import de.treichels.hott.model.BaseModel
import de.treichels.hott.model.HelicopterModel
import de.treichels.hott.model.WingedModel
import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.model.voice.VoiceFile
import freemarker.template.Configuration
import freemarker.template.DefaultObjectWrapper
import freemarker.template.TemplateException
import freemarker.template.TemplateExceptionHandler
import org.apache.commons.io.IOUtils
import tornadofx.*
import java.io.*
import java.util.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
object HTMLReport {
    private val CONFIGURATION: Configuration = Configuration(Configuration.VERSION_2_3_27)
    private var CURVE_IMAGE_GENERATOR = ServiceLoader.load(CurveImageGenerator::class.java).firstOrNull()
            ?: DummyCurveImageGenerator()

    init {
        // setup freemarker
        CONFIGURATION.setEncoding(Locale.getDefault(), "UTF-8")
        CONFIGURATION.objectWrapper = DefaultObjectWrapper(Configuration.VERSION_2_3_27)

        // extract font file
        val fontFile = File(System.getProperty("java.io.tmpdir"), "Arial.ttf")

        ResourceLookup(HTMLReport).stream("Arial.ttf").use { inputStream ->
            FileOutputStream(fontFile).use { outputStream ->
                IOUtils.copy(inputStream, outputStream)
            }
        }
    }

    @Throws(IOException::class, ReportException::class)
    fun generateHTML(model: BaseModel): String {
        CONFIGURATION.setClassForTemplateLoading(HTMLReport::class.java, "templates/hott")

        val rootMap = mutableMapOf(Pair("model", model), Pair("png", CURVE_IMAGE_GENERATOR))

        when (model) {
        // TODO: add other model types
            is WingedModel -> rootMap["wingedModel"] = model
            is HelicopterModel -> rootMap["helicopterModel"] = model
        }

        val template = when (model.transmitterType) {
            TransmitterType.mc16, TransmitterType.mc20, TransmitterType.mc26, TransmitterType.mc28, TransmitterType.mc32, TransmitterType.mx20 -> "mc-32.xhtml"
            TransmitterType.mx12, TransmitterType.mx16 -> "mx-16.xhtml"
            else -> return "<html><head>Not Implemented</head><body><h1>${model.transmitterType} is not yet supported!</h1></body></html>"
        }

        return genetateHTML(template, rootMap)
    }

    @Throws(IOException::class)
    fun generateHTML(name: String, title: String, version: String, voiceFile: VoiceFile): String {
        CONFIGURATION.setClassForTemplateLoading(HTMLReport::class.java, "templates/hott")

        val rootMap = HashMap<String, Any>()
        rootMap["name"] = name
        rootMap["title"] = title
        rootMap["version"] = version
        rootMap["voicefile"] = voiceFile

        val baos = ByteArrayOutputStream()
        val template = CONFIGURATION.getTemplate("voicefile.ftl")

        try {
            template.process(rootMap, OutputStreamWriter(baos, "UTF-8"))
        } catch (e: TemplateException) {
            throw ReportException(e)
        }

        return baos.toString()
    }

    @Throws(IOException::class)
    private fun genetateHTML(templateName: String, rootMap: MutableMap<String, Any>): String {
        return try {
            val baos = ByteArrayOutputStream()
            val template = HTMLReport.CONFIGURATION.getTemplate(templateName)

            rootMap["hex"] = FreeMarkerHexConverter()
            rootMap["htmlsafe"] = FreeMarkerHtmlSafeDirective()
            rootMap["programDir"] = File(System.getProperty("program.dir", ".")).toURI().toURL().toString()
            rootMap["fontFile"] = File(System.getProperty("java.io.tmpdir"), "Arial.ttf").toURI().toURL().toString()
            rootMap["version"] = System.getProperty("program.version", "unknown")

            template.process(rootMap, OutputStreamWriter(baos, "UTF-8"))
            baos.toString()
        } catch (e: TemplateException) {
            throw ReportException(e)
        }

    }

    @Throws(IOException::class)
    fun save(file: File, html: String) = FileWriter(file).use { it.write(html) }

    fun setSuppressExceptions(suppress: Boolean) {
        CONFIGURATION.templateExceptionHandler = if (suppress) FreeMarkerExceptionHandler() else TemplateExceptionHandler.DEBUG_HANDLER
    }
}
