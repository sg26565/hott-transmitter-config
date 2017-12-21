/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.report.pdf;

import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.pdf.ITextReplacedElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.codec.Base64;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class ITextInlineImageReplacedElementFactory implements ReplacedElementFactory {
    private static final String PREFIX = "data:image/png;base64,"; //$NON-NLS-1$

    private final ReplacedElementFactory other;

    public ITextInlineImageReplacedElementFactory(final ReplacedElementFactory other) {
        this.other = other;
    }

    @Override
    public ReplacedElement createReplacedElement(final LayoutContext c, final BlockBox box, final UserAgentCallback uac, final int cssWidth,
            final int cssHeight) {
        final Element elem = box.getElement();

        // check if we have an inline png image
        if (!(elem != null && elem.getNodeName().equals("img") && elem.hasAttribute("src") && elem.getAttribute("src").startsWith(PREFIX))) return null; // other.createReplacedElement(c,
                                                                                                                                                         // box,
                                                                                                                                                         // uac,
                                                                                                                                                         // cssWidth,
        // cssHeight);

        int width = 0;
        int height = 0;

        if (cssWidth > 0) width = cssWidth;
        if (cssHeight > 0) height = cssHeight;

        if (elem.hasAttribute("width")) width = Integer.parseInt(elem.getAttribute("width")); //$NON-NLS-1$

        if (elem.hasAttribute("height")) height = Integer.parseInt(elem.getAttribute("height")); //$NON-NLS-1$

        final String inlineData = elem.getAttribute("src").substring(PREFIX.length()); // strip //$NON-NLS-1$
                                                                                       // leading
                                                                                       // "data:image/png;base64,"

        try {
            final Image image = Image.getInstance(Base64.decode(inlineData));

            if (width == 0) width = (int) image.getWidth();

            if (height == 0) height = (int) image.getHeight();

            image.scaleAbsolute(width * 16, height * 16);

            final ITextFSImage fsImage = new ITextFSImage(image);
            final ITextReplacedElement element = new ITextImageElement(fsImage);
            return element;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(final Element e) {
        other.remove(e);
    }

    @Override
    public void reset() {
        other.reset();
    }

    @Override
    public void setFormSubmissionListener(final FormSubmissionListener listener) {
        other.setFormSubmissionListener(listener);
    }
}