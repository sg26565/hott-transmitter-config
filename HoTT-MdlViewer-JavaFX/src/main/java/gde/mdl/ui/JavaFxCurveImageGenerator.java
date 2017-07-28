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
package gde.mdl.ui;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import gde.model.Curve;
import gde.model.CurvePoint;
import gde.report.CurveImageGenerator;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;

/**
 * Generate offline PNG image using Swing.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class JavaFxCurveImageGenerator implements CurveImageGenerator {
    private Image image;

    @Override
    public String getImageSource(final Curve curve, final float scale, final boolean description) {
        final boolean pitchCurve = curve.getPoint()[0].getPosition() == 0;
        final Canvas canvas = new Canvas(10 + 200 * scale, 10 + 250 * scale);
        final GraphicsContext g = canvas.getGraphicsContext2D();

        // clear background
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, 10 + 200 * scale, 10 + 250 * scale);

        // draw out limit rect
        g.setStroke(Color.BLACK);
        g.strokeRect(5, 5, 200 * scale, 250 * scale);

        // dashed gray lines
        g.setLineWidth(1);
        g.setLineCap(StrokeLineCap.BUTT);
        g.setLineJoin(StrokeLineJoin.ROUND);
        g.setMiterLimit(0);
        g.setLineDashes(5, 5);
        g.setStroke(Color.GRAY);

        // +100% and -100% horizontal lines
        g.strokeLine(5, 5 + 25 * scale, 5 + 200 * scale, 5 + 25 * scale);
        g.strokeLine(5, 5 + 225 * scale, 5 + 200 * scale, 5 + 225 * scale);

        if (!pitchCurve) {
            // horizontal and vertical 0% lines
            g.strokeLine(5, 5 + 125 * scale, 5 + 200 * scale, 5 + 125 * scale);
            g.strokeLine(5 + 100 * scale, 5, 5 + 100 * scale, 5 + 250 * scale);
        }

        if (curve.getPoint() != null) {
            g.setLineWidth(1);
            g.setLineDashes(null);
            g.setFont(Font.font("Arial", 12)); //$NON-NLS-1$
            g.setStroke(Color.BLACK);

            int numPoints = 0;
            for (final CurvePoint p : curve.getPoint())
                if (p.isEnabled()) numPoints++;

            final double[] xVals = new double[numPoints];
            final double[] yVals = new double[numPoints];

            int i = 0;
            for (final CurvePoint p : curve.getPoint())
                if (p.isEnabled()) {
                    if (i == 0)
                        xVals[i] = pitchCurve ? 0 : -100;
                    else if (i == numPoints - 1)
                        xVals[i] = 100;
                    else
                        xVals[i] = p.getPosition();
                    yVals[i] = p.getValue();

                    // draw dot and point number
                    if (description) {
                        double x0;
                        double y0;
                        if (pitchCurve) {
                            x0 = 5 + xVals[i] * 2 * scale;
                            y0 = 5 + (225 - yVals[i] * 2) * scale;
                        } else {
                            x0 = 5 + (100 + xVals[i]) * scale;
                            y0 = 5 + (125 - yVals[i]) * scale;
                        }

                        g.strokeOval(x0 - 2, y0 - 2, 4, 4);
                        g.clearRect(x0 - 6, y0 - 16, 8, 12);
                        g.strokeText(Integer.toString(p.getNumber() + 1), x0 - 3, y0 - 5);
                    }

                    i++;
                }

            g.setLineWidth(2);

            if (numPoints > 2 && curve.isSmoothing()) {
                // spline interpolate the curve points
                final SplineInterpolator s = new SplineInterpolator();
                final PolynomialSplineFunction function = s.interpolate(xVals, yVals);

                g.beginPath();

                if (pitchCurve) {
                    g.moveTo(5, 5 + (225 - yVals[0] * 2) * scale);
                    for (double x = 6; x < 4 + 200 * scale; x++)
                        g.lineTo(x, 5 + (225 - function.value((x - 5) / scale / 2) * 2) * scale);
                } else {
                    g.moveTo(5, 5 + (125 - yVals[0]) * scale);
                    for (double x = 6; x < 4 + 200 * scale; x++)
                        g.lineTo(x, 5 + (125 - function.value((x - 5) / scale - 100)) * scale);
                }

                g.stroke();
            } else {
                g.beginPath();

                if (pitchCurve) {
                    g.moveTo(5 + xVals[0] * 2 * scale, 5 + (225 - yVals[0] * 2) * scale);
                    for (i = 1; i < numPoints; i++)
                        g.lineTo(5 + xVals[i] * 2 * scale, 5 + (225 - yVals[i] * 2) * scale);
                } else {
                    g.moveTo(5 + (100 + xVals[0]) * scale, 5 + (125 - yVals[0]) * scale);
                    for (i = 1; i < numPoints; i++)
                        g.lineTo(5 + (100 + xVals[i]) * scale, 5 + (125 - yVals[i]) * scale);
                }

                g.stroke();
            }
        }

        // run canvas.snapshot on the FX Application thread, suspend this thread
        // and wait for completion
        synchronized (this) {
            Platform.runLater(() -> {
                image = canvas.snapshot(null, null);
                synchronized (JavaFxCurveImageGenerator.this) {
                    JavaFxCurveImageGenerator.this.notify();
                }
            });
            try {
                wait();
            } catch (final InterruptedException e) {
                ExceptionDialog.show(e);
            }
        }

        final RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);

        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(renderedImage, "png", baos); //$NON-NLS-1$
            return PREFIX + Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
