/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.mdlviewer.swt;

import de.treichels.hott.model.Curve;
import de.treichels.hott.model.CurvePoint;
import de.treichels.hott.report.html.CurveImageGenerator;
import de.treichels.hott.util.Util;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * Generate offline PNG image using SWT.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class SwtCurveImageGenerator implements CurveImageGenerator {
    private Image getImage(final Curve curve, final double scale, final boolean description) {
        // pitch curves start with 0% instead of -100%
        final boolean pitchCurve = curve.getPoint().get(0).getPosition() == 0;

        Image image;
        GC g = null;

        try {
            // 200x250 pixel image with 5 pixel border scaled by factor scale
            image = new Image(Display.getDefault(), (int) (10 + 200 * scale), (int) (10 + 250 * scale));
            g = new GC(image);
            g.setAntialias(SWT.ON);
            g.setTextAntialias(SWT.ON);

            // clear background
            g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
            g.fillRectangle(0, 0, (int) (10 + 200 * scale), (int) (10 + 250 * scale));

            // outer rectangle
            g.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
            g.setLineWidth(1);
            g.drawRectangle(5, 5, (int) (200 * scale), (int) (250 * scale));

            g.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));

            // +100% horizontal line
            g.drawLine(5, (int) (5 + 25 * scale), (int) (5 + 200 * scale), (int) (5 + 25 * scale));

            // -100% horizontal line
            g.drawLine(5, (int) (5 + 225 * scale), (int) (5 + 200 * scale), (int) (5 + 225 * scale));

            if (!pitchCurve) {
                // 0% horizontal line
                g.drawLine(5, (int) (5 + 125 * scale), (int) (5 + 200 * scale), (int) (5 + 125 * scale));

                // 0% vertical line
                g.drawLine((int) (5 + 100 * scale), 5, (int) (5 + 100 * scale), (int) (5 + 250 * scale));
            }

            g.setFont(new Font(Display.getDefault(), new FontData("Arial", 10, SWT.BOLD)));
            g.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));

            // determine number of enabled curve points
            int numPoints = 0;
            for (final CurvePoint p : curve.getPoint())
                if (p.isEnabled()) numPoints++;

            final double[] xVals = new double[numPoints];
            final double[] yVals = new double[numPoints];
            int i = 0;

            // store coordinates
            for (final CurvePoint p : curve.getPoint())
                if (p.isEnabled()) {
                    if (i == 0)
                        // first point x coordinate is fixed to -100% (0%
                        // for pitch curve)
                        xVals[i] = pitchCurve ? 0 : -100;
                    else if (i == numPoints - 1) // last point x coordinate is fixed to +100%
                        xVals[i] = 100;
                    else
                        xVals[i] = p.getPosition();
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

                        // draw point
                        g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
                        g.fillOval(x0 - 3, y0 - 3, 6, 6);

                        // draw text
                        g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
                        g.fillRectangle(x0 - 5, y0 + 3, 9, 13);
                        g.drawText(Integer.toString(p.getNumber() + 1), x0 - 4, y0 + 2, true);
                    }

                    i++;
                }

            g.setLineWidth(2);

            if (numPoints > 2 && curve.isSmoothing()) {
                // use a spline interpolator to smooth the curve
                final SplineInterpolator s = new SplineInterpolator();
                final PolynomialSplineFunction function = s.interpolate(xVals, yVals);

                int x0 = 5;
                int y0;

                // starting point screen coordinates
                if (pitchCurve)
                    y0 = (int) (5 + (225 - yVals[0] * 2) * scale);
                else
                    y0 = (int) (5 + (125 - yVals[0]) * scale);

                // draw line pixel-by-pixel
                while (x0 < (int) (4 + 200 * scale)) {
                    final int x1 = x0 + 1;
                    int y1;

                    if (pitchCurve)
                        y1 = (int) (5 + (225 - function.value((x1 - 5) / scale / 2) * 2) * scale);
                    else
                        y1 = (int) (5 + (125 - function.value((x1 - 5) / scale - 100)) * scale);

                    g.drawLine(x0, y0, x1, y1);

                    x0 = x1;
                    y0 = y1;
                }
            } else
                // draw line segments
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
        } finally {
            if (g != null) g.dispose();
        }

        return image;
    }

    @NotNull
    @Override
    public String getImageSource(@NotNull final Curve curve, final double scale, final boolean description) {
        final Image image = getImage(curve, scale, description);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ImageLoader imageLoader = new ImageLoader();
        try {
            imageLoader.data = new ImageData[] { image.getImageData() };
            imageLoader.save(baos, SWT.IMAGE_PNG);
        } catch (final Exception e) {
            if (Util.INSTANCE.getDEBUG()) e.printStackTrace();
        }

        return CurveImageGenerator.Companion.PREFIX + Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
