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
package gde.mdl.ui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;
import org.xhtmlrenderer.swing.ImageReplacedElement;

/**
 * @author oli@treichels.de
 */
public class InlineImageReplacedElementFactory implements ReplacedElementFactory {
  private static final String PREFIX = "data:image/png;base64,";

  @Override
  public ReplacedElement createReplacedElement(final LayoutContext c, final BlockBox box, final UserAgentCallback uac, final int cssWidth, final int cssHeight) {
    final Element elem = box.getElement();

    // check if we have an inline png image
    if (!(elem != null && elem.getNodeName().equals("img") && elem.hasAttribute("src") && elem.getAttribute("src").startsWith(PREFIX))) {
      return null;
    }

    int width = 0;
    int height = 0;

    if (cssWidth > 0) {
      width = cssWidth;
    }
    if (cssHeight > 0) {
      height = cssHeight;
    }

    if (elem.hasAttribute("width")) {
      width = Integer.parseInt(elem.getAttribute("width"));
    }

    if (elem.hasAttribute("height")) {
      height = Integer.parseInt(elem.getAttribute("height"));
    }

    final String inlineData = elem.getAttribute("src").substring(PREFIX.length()); // strip
                                                                                   // leading
                                                                                   // "data:image/png;base64,"

    try {
      final ByteArrayInputStream is = new ByteArrayInputStream(Base64.decodeBase64(inlineData));
      final BufferedImage image = ImageIO.read(is);

      if (width == 0) {
        width = image.getWidth();
      }

      if (height == 0) {
        height = image.getHeight();
      }

      final ImageReplacedElement element = new ImageReplacedElement(image, width, height);
      return element;
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(final Element e) {}

  @Override
  public void reset() {}

  @Override
  public void setFormSubmissionListener(final FormSubmissionListener listener) {}
}