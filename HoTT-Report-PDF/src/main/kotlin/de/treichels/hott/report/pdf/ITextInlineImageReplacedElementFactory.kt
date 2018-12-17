/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.report.pdf

import com.itextpdf.text.Image
import com.itextpdf.text.pdf.codec.Base64
import org.w3c.dom.Element
import org.xhtmlrenderer.extend.ReplacedElement
import org.xhtmlrenderer.extend.ReplacedElementFactory
import org.xhtmlrenderer.extend.UserAgentCallback
import org.xhtmlrenderer.layout.LayoutContext
import org.xhtmlrenderer.pdf.ITextFSImage
import org.xhtmlrenderer.pdf.ITextImageElement
import org.xhtmlrenderer.render.BlockBox
import org.xhtmlrenderer.simple.extend.FormSubmissionListener

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
internal class ITextInlineImageReplacedElementFactory(private val other: ReplacedElementFactory) : ReplacedElementFactory {
    override fun createReplacedElement(c: LayoutContext, box: BlockBox, uac: UserAgentCallback, cssWidth: Int, cssHeight: Int): ReplacedElement? {
        val elem = box.element

        // check if we have an inline png image
        if (!(elem != null && elem.nodeName == "img" && elem.hasAttribute("src") && elem.getAttribute("src").startsWith(PREFIX))) return null

        var width = 0
        var height = 0

        if (cssWidth > 0) width = cssWidth
        if (cssHeight > 0) height = cssHeight

        if (elem.hasAttribute("width")) width = Integer.parseInt(elem.getAttribute("width"))
        if (elem.hasAttribute("height")) height = Integer.parseInt(elem.getAttribute("height"))

        // strip leading "data:image/png;base64,"
        val inlineData = elem.getAttribute("src").substring(PREFIX.length)

        return try {
            val image = Image.getInstance(Base64.decode(inlineData))
            if (width == 0) width = image.width.toInt()
            if (height == 0) height = image.height.toInt()

            image.scaleAbsolute((width * 16).toFloat(), (height * 16).toFloat())

            val fsImage = ITextFSImage(image)
            ITextImageElement(fsImage)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun remove(e: Element) {
        other.remove(e)
    }

    override fun reset() {
        other.reset()
    }

    override fun setFormSubmissionListener(listener: FormSubmissionListener) {
        other.setFormSubmissionListener(listener)
    }

    companion object {
        private const val PREFIX = "data:image/png;base64,"
    }
}