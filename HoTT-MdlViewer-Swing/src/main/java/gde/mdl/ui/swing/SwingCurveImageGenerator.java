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
package gde.mdl.ui.swing;

import gde.model.Curve;
import gde.model.CurvePoint;
import gde.report.CurveImageGenerator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

/**
 * @author oli@treichels.de
 */
public class SwingCurveImageGenerator implements CurveImageGenerator {
  private static final String PREFIX = "data:image/png;base64,";

  private BufferedImage getImage(final Curve curve, final float scale, final boolean description) {
    final boolean pitchCurve = curve.getPoint()[0].getPosition() == 0;

    final BufferedImage image = new BufferedImage((int) (10 + 200 * scale), (int) (10 + 250 * scale), BufferedImage.TYPE_INT_RGB);

    Graphics2D g = null;

    try {
      g = image.createGraphics();

      g.setBackground(Color.WHITE);
      g.clearRect(0, 0, (int) (10 + 200 * scale), (int) (10 + 250 * scale));

      g.setColor(Color.BLACK);
      g.drawRect(5, 5, (int) (200 * scale), (int) (250 * scale));

      g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0.0f, new float[] { 5.0f, 5.0f }, 0.0f));
      g.setColor(Color.GRAY);

      g.drawLine(5, (int) (5 + 25 * scale), (int) (5 + 200 * scale), (int) (5 + 25 * scale));
      g.drawLine(5, (int) (5 + 225 * scale), (int) (5 + 200 * scale), (int) (5 + 225 * scale));
      if (!pitchCurve) {
        g.drawLine(5, (int) (5 + 125 * scale), (int) (5 + 200 * scale), (int) (5 + 125 * scale));
        g.drawLine((int) (5 + 100 * scale), 5, (int) (5 + 100 * scale), (int) (5 + 250 * scale));
      }

      if (curve.getPoint() != null) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0.0f, null, 0.0f));
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.setColor(Color.BLACK);

        int numPoints = 0;
        for (final CurvePoint p : curve.getPoint()) {
          if (p.isEnabled()) {
            numPoints++;
          }
        }

        final double[] xVals = new double[numPoints];
        final double[] yVals = new double[numPoints];

        int i = 0;
        for (final CurvePoint p : curve.getPoint()) {
          if (p.isEnabled()) {
            if (i == 0) {
              xVals[i] = pitchCurve ? 0 : -100;
            } else if (i == numPoints - 1) {
              xVals[i] = 100;
            } else {
              xVals[i] = p.getPosition();
            }
            yVals[i] = p.getValue();

            if (description) {
              int x0;
              int y0;
              if (pitchCurve) {
                x0 = (int) (5 + xVals[i] * 2 * scale);
                y0 = (int) (5 + (225 - yVals[i] * 2) * scale);
              } else {
                x0 = (int) (5 + (100 + xVals[i]) * scale);
                y0 = (int) (5 + (125 - yVals[i]) * scale);
              }

              g.drawOval(x0 - 2, y0 - 2, 4, 4);
              g.clearRect(x0 - 6, y0 - 16, 8, 12);
              g.drawString(Integer.toString(p.getNumber() + 1), x0 - 3, y0 - 5);
            }

            i++;
          }
        }

        g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0.0f, null, 0.0f));

        if (numPoints > 2 && curve.isSmoothing()) {
          final SplineInterpolator s = new SplineInterpolator();
          final PolynomialSplineFunction function = s.interpolate(xVals, yVals);

          int x0 = 5;
          int y0;
          if (pitchCurve) {
            y0 = (int) (5 + (225 - yVals[0] * 2) * scale);
          } else {
            y0 = (int) (5 + (125 - yVals[0]) * scale);
          }

          while (x0 < (int) (4 + 200 * scale)) {
            final int x1 = x0 + 1;
            int y1;
            if (pitchCurve) {
              y1 = (int) (5 + (225 - function.value((x1 - 5) / scale / 2) * 2) * scale);
            } else {
              y1 = (int) (5 + (125 - function.value((x1 - 5) / scale - 100)) * scale);
            }

            g.drawLine(x0, y0, x1, y1);

            x0 = x1;
            y0 = y1;
          }
        } else {
          for (i = 0; i < numPoints - 1; i++) {
            int x0, y0, x1, y1;

            if (pitchCurve) {
              x0 = (int) (5 + xVals[i] * 2 * scale);
              y0 = (int) (5 + (225 - yVals[i] * 2) * scale);

              x1 = (int) (5 + xVals[i + 1] * 2 * scale);
              y1 = (int) (5 + (225 - yVals[i + 1] * 2) * scale);
            } else {
              x0 = (int) (5 + (100 + xVals[i]) * scale);
              y0 = (int) (5 + (125 - yVals[i]) * scale);

              x1 = (int) (5 + (100 + xVals[i + 1]) * scale);
              y1 = (int) (5 + (125 - yVals[i + 1]) * scale);
            }

            g.drawLine(x0, y0, x1, y1);
          }
        }
      }
    } finally {
      if (g != null) {
        g.dispose();
      }
    }

    return image;
  }

  /*
   * (non-Javadoc)
   * 
   * @see gde.report.CurveImageGenerator#getImageSource(gde.model.Curve, float,
   * boolean)
   */
  @Override
  public String getImageSource(final Curve curve, final float scale, final boolean description) {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ImageIO.write(getImage(curve, scale, description), "png", baos);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return PREFIX + Base64.encodeBase64String(baos.toByteArray());
  }
}
