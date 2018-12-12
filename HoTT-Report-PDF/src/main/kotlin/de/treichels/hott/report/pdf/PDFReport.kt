/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.treichels.hott.report.pdf

import com.itextpdf.text.DocumentException
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
object PDFReport {
    @Throws(IOException::class)
    fun save(file: File, html: String) {
        FileOutputStream(file).use { fos -> save(fos, html) }
    }

    @Throws(IOException::class)
    private fun save(os: OutputStream, html: String) {
        try {
            // setup flyingsaucer
            val renderer = ITextRenderer()
            val ctx = renderer.sharedContext

            ctx.replacedElementFactory = ITextInlineImageReplacedElementFactory(ctx.replacedElementFactory)

            renderer.setDocumentFromString(html)
            renderer.layout()
            renderer.createPDF(os)
        } catch (e: DocumentException) {
            // wrap into standard IOException
            throw IOException(e)
        }
    }
}
