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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

/**
 * @author oli@treichels.de
 */
public class BaseSVGReplacedElementFactory implements ReplacedElementFactory {
	private static final SAXSVGDocumentFactory	SVG_DOCUMENT_FACTORY;
	private static final Transformer						XML_TRANSFORMER;

	static {
		try {
			SVG_DOCUMENT_FACTORY = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
			XML_TRANSFORMER = TransformerFactory.newInstance().newTransformer();
			XML_TRANSFORMER.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			XML_TRANSFORMER.setOutputProperty(OutputKeys.INDENT, "yes");
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final ReplacedElementFactory				other;

	public BaseSVGReplacedElementFactory(final ReplacedElementFactory other) {
		this.other = other;
	}

	@Override
	public ReplacedElement createReplacedElement(final LayoutContext c, final BlockBox box, final UserAgentCallback uac, final int cssWidth, final int cssHeight) {
		return other.createReplacedElement(c, box, uac, cssWidth, cssHeight);
	}

	protected String getString(final Node node) {
		try {
			final StringWriter buffer = new StringWriter();
			XML_TRANSFORMER.transform(new DOMSource(node), new StreamResult(buffer));
			return buffer.toString();
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected SVGDocument getSVGDocument(final String data) {
		final StringReader r = new StringReader(data);
		SVGDocument doc;
		try {
			doc = SVG_DOCUMENT_FACTORY.createSVGDocument(null, r);
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			r.close();
		}
		return doc;
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
