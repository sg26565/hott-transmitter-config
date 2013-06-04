/**
 *  HoTT Transmitter Config
 *  Copyright (C) 2013  Oliver Treichel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package gde.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.swt.ImageReplacedElement;
import org.xhtmlrenderer.swt.SWTFSImage;

/**
 * @author oli@treichels.de
 */
public class SWTSVGReplacedElementFactory extends BaseSVGReplacedElementFactory {
	public SWTSVGReplacedElementFactory(final ReplacedElementFactory other) {
		super(other);
	}

	@Override
	public ReplacedElement createReplacedElement(final LayoutContext c, final BlockBox box, final UserAgentCallback uac, final int cssWidth, final int cssHeight) {
		final Element elem = box.getElement();

		if (elem == null || !elem.getNodeName().equals("svg")) {
			return super.createReplacedElement(c, box, uac, cssWidth, cssHeight);
		}

		float width = -1;
		float height = -1;

		if (cssWidth > 0) {
			width = cssWidth;
		}
		if (cssHeight > 0) {
			height = cssHeight;
		}

		String val = elem.getAttribute("width");
		if (val != null && val.length() > 0) {
			width = Float.valueOf(val).intValue();
		}
		val = elem.getAttribute("height");
		if (val != null && val.length() > 0) {
			height = Float.valueOf(val).intValue();
		}

		final String data = getString(elem);
		SVGDocument doc = getSVGDocument(data);
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final TranscoderInput input = new TranscoderInput(doc);
		final TranscoderOutput output = new TranscoderOutput(os);
		final Transcoder t = new PNGTranscoder();

		try {
			t.transcode(input, output);
			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
			final Image image = new Image(Display.getCurrent(), is);
			final SWTFSImage fsImage = new SWTFSImage(image, uac, "");
			final ImageReplacedElement element = new ImageReplacedElement(fsImage, (int)width, (int)height);
			return element;
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
