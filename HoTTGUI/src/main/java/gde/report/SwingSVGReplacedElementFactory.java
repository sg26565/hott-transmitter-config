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

import java.awt.Container;
import java.awt.Dimension;

import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.swing.SwingReplacedElement;

/**
 * @author oli@treichels.de
 */
public class SwingSVGReplacedElementFactory extends BaseSVGReplacedElementFactory {
	public SwingSVGReplacedElementFactory(final ReplacedElementFactory other) {
		super(other);
	}

	@Override
	public ReplacedElement createReplacedElement(final LayoutContext c, final BlockBox box, final UserAgentCallback uac, final int cssWidth, final int cssHeight) {
		final Element elem = box.getElement();

		if (elem == null || !elem.getNodeName().equals("svg")) {
			return super.createReplacedElement(c, box, uac, cssWidth, cssHeight);
		}

		final String data = getString(elem);
		final SVGDocument doc = getSVGDocument(data);

		final JSVGCanvas svgCanvas = new JSVGCanvas();
		svgCanvas.setSVGDocument(doc);

		int width = 0;
		int height = 0;

		if (svgCanvas.getSVGDocumentSize() != null) {
			width = (int) svgCanvas.getSVGDocumentSize().getWidth();
			height = (int) svgCanvas.getSVGDocumentSize().getHeight();
		}

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

		final Dimension size = new Dimension(width, height);
		svgCanvas.setPreferredSize(size);
		svgCanvas.setSize(size);

		if (c.isInteractive()) {
			((Container) c.getCanvas()).add(svgCanvas);
		}

		return new SwingReplacedElement(svgCanvas);
	}
}
