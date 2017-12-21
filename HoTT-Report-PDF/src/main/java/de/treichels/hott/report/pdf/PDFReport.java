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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.DocumentException;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class PDFReport {
    public static void save(final File file, final String html) throws IOException, DocumentException {
        try (final FileOutputStream fos = new FileOutputStream(file)) {
            save(fos, html);
        }
    }

    public static void save(final OutputStream os, final String html) throws IOException, DocumentException {
        // setup flyingsaucer
        final ITextRenderer renderer = new ITextRenderer();
        final SharedContext ctx = renderer.getSharedContext();

        ctx.setReplacedElementFactory(new ITextInlineImageReplacedElementFactory(ctx.getReplacedElementFactory()));

        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(os);
    }
}
