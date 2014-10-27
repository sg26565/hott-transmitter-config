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
package de.treichels.hott.ui.android.html;

import gde.model.Curve;
import gde.model.CurvePoint;
import gde.report.CurveImageGenerator;

import java.io.ByteArrayOutputStream;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.Base64;

/**
 * Android implementation of a {@link CurveImageGenerator}.
 *
 * @author oli@treichels.de
 */
public class AndroidCurveImageGenerator implements CurveImageGenerator {
    private Bitmap getBitmap(final Curve curve, final float scale, final boolean description) {
        final boolean pitchCurve = curve.getPoint()[0].getPosition() == 0;
        final float scale1 = scale * 0.75f; // smaller images on the android
        // platform

        final Bitmap image = Bitmap.createBitmap((int) (10 + 200 * scale1), (int) (10 + 250 * scale1), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(image);

        final Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Style.FILL);

        final Paint forgroundPaint = new Paint();
        forgroundPaint.setColor(Color.BLACK);
        forgroundPaint.setStyle(Style.STROKE);
        forgroundPaint.setStrokeWidth(1.0f);
        forgroundPaint.setStrokeCap(Cap.BUTT);
        forgroundPaint.setStrokeJoin(Join.ROUND);
        forgroundPaint.setStrokeMiter(0.0f);

        final Paint curvePaint = new Paint(forgroundPaint);
        curvePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        curvePaint.setStrokeWidth(2.0f);

        final Paint pointPaint = new Paint(curvePaint);
        pointPaint.setStrokeWidth(5.0f);
        pointPaint.setStyle(Style.FILL_AND_STROKE);

        final Paint helpLinePaint = new Paint(forgroundPaint);
        helpLinePaint.setColor(Color.GRAY);
        helpLinePaint.setPathEffect(new DashPathEffect(new float[] { 5.0f, 5.0f }, 2.5f));

        final Paint textPaint = new Paint(forgroundPaint);
        textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        textPaint.setTextSize(12.0f);
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setStyle(Style.FILL);

        canvas.drawRect(0, 0, 10 + 200 * scale1, 10 + 250 * scale1, backgroundPaint);
        canvas.drawRect(5, 5, 5 + 200 * scale1, 5 + 250 * scale1, forgroundPaint);

        canvas.drawLine(5, 5 + 25 * scale1, 5 + 200 * scale1, 5 + 25 * scale1, helpLinePaint);
        canvas.drawLine(5, 5 + 225 * scale1, 5 + 200 * scale1, 5 + 225 * scale1, helpLinePaint);
        if (!pitchCurve) {
            canvas.drawLine(5, 5 + 125 * scale1, 5 + 200 * scale1, 5 + 125 * scale1, helpLinePaint);
            canvas.drawLine(5 + 100 * scale1, 5, 5 + 100 * scale1, 5 + 250 * scale1, helpLinePaint);
        }

        if (curve.getPoint() != null) {
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
                        float x0;
                        float y0;
                        if (pitchCurve) {
                            x0 = (float) (5 + xVals[i] * 2 * scale1);
                            y0 = (float) (5 + (225 - yVals[i] * 2) * scale1);
                        } else {
                            x0 = (float) (5 + (100 + xVals[i]) * scale1);
                            y0 = (float) (5 + (125 - yVals[i]) * scale1);
                        }

                        canvas.drawPoint(x0, y0, pointPaint);
                        if (y0 < 5 + 125 * scale1) {
                            canvas.drawRect(x0 - 4, y0 + 5, x0 + 3, y0 + 18, backgroundPaint);
                            canvas.drawText(Integer.toString(p.getNumber() + 1), x0 - 1, y0 + 16, textPaint);
                        } else {
                            canvas.drawRect(x0 - 4, y0 - 5, x0 + 3, y0 - 18, backgroundPaint);
                            canvas.drawText(Integer.toString(p.getNumber() + 1), x0 - 1, y0 - 7, textPaint);
                        }
                    }

                    i++;
                }
            }

            if (numPoints > 2 && curve.isSmoothing()) {
                final SplineInterpolator s = new SplineInterpolator();
                final PolynomialSplineFunction function = s.interpolate(xVals, yVals);

                float x0 = 5;
                float y0;
                if (pitchCurve) {
                    y0 = (float) (5 + (225 - yVals[0] * 2) * scale1);
                } else {
                    y0 = (float) (5 + (125 - yVals[0]) * scale1);
                }

                while (x0 < 4 + 200 * scale1) {
                    final float x1 = x0 + 1;
                    float y1;
                    if (pitchCurve) {
                        y1 = (float) (5 + (225 - function.value((x1 - 5) / scale1 / 2) * 2) * scale1);
                    } else {
                        y1 = (float) (5 + (125 - function.value((x1 - 5) / scale1 - 100)) * scale1);
                    }

                    canvas.drawLine(x0, y0, x1, y1, curvePaint);

                    x0 = x1;
                    y0 = y1;
                }
            } else {
                for (i = 0; i < numPoints - 1; i++) {
                    float x0, y0, x1, y1;

                    if (pitchCurve) {
                        x0 = (float) (5 + xVals[i] * 2 * scale1);
                        y0 = (float) (5 + (225 - yVals[i] * 2) * scale1);

                        x1 = (float) (5 + xVals[i + 1] * 2 * scale1);
                        y1 = (float) (5 + (225 - yVals[i + 1] * 2) * scale1);
                    } else {
                        x0 = (float) (5 + (100 + xVals[i]) * scale1);
                        y0 = (float) (5 + (125 - yVals[i]) * scale1);

                        x1 = (float) (5 + (100 + xVals[i + 1]) * scale1);
                        y1 = (float) (5 + (125 - yVals[i + 1]) * scale1);
                    }

                    canvas.drawLine(x0, y0, x1, y1, curvePaint);
                }
            }
        }

        return image;
    }

    @Override
    public String getImageSource(final Curve curve, final float scale, final boolean description) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        getBitmap(curve, scale, description).compress(CompressFormat.PNG, 100, baos);
        return PREFIX + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
}