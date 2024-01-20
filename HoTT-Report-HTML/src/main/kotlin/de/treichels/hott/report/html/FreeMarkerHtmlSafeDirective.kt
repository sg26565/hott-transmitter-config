/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.treichels.hott.report.html

import freemarker.core.Environment
import freemarker.template.TemplateDirectiveBody
import freemarker.template.TemplateDirectiveModel
import freemarker.template.TemplateException
import freemarker.template.TemplateModel

import java.io.CharArrayWriter
import java.io.IOException
import java.io.Writer

internal class FreeMarkerHtmlSafeDirective : TemplateDirectiveModel {
    private class HtmlSafeWriter internal constructor(private val writer: Writer) : Writer() {
        @Throws(IOException::class)
        override fun close() {
            writer.close()
        }

        @Throws(IOException::class)
        override fun flush() {
            writer.flush()
        }

        @Throws(IOException::class)
        override fun write(cbuf: CharArray, off: Int, len: Int) {
            val w = CharArrayWriter()

            (0 until len)
                    .map { cbuf[it + off] }
                    .forEach {
                        when (it) {
                            'ä' -> w.write("&auml;")

                            'ö' -> w.write("&ouml;")

                            'ü' -> w.write("&uuml;")

                            'Ä' -> w.write("&Auml;")

                            'Ö' -> w.write("&Ouml;")

                            'Ü' -> w.write("&Uuml;")

                            'ß' -> w.write("&szlig;")

                            '€' -> w.write("&euro;")

                            else -> w.write(it.code)
                        }
                    }

            writer.write(w.toCharArray())
        }
    }

    @Throws(TemplateException::class, IOException::class)
    override fun execute(env: Environment, params: Map<*, *>, loopVars: Array<TemplateModel>, body: TemplateDirectiveBody?) {
        body?.render(HtmlSafeWriter(env.out))
    }
}