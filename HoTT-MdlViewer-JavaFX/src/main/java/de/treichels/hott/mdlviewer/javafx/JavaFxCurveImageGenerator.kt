/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package de.treichels.hott.mdlviewer.javafx

import de.treichels.hott.model.Curve
import de.treichels.hott.report.html.CurveImageGenerator
import javafx.embed.swing.SwingFXUtils
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.shape.StrokeLineCap
import javafx.scene.shape.StrokeLineJoin
import javafx.scene.text.Font
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator
import tornadofx.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.CountDownLatch
import javax.imageio.ImageIO

/**
 * Generate offline PNG image using JavaFX.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
class JavaFxCurveImageGenerator : CurveImageGenerator {
    private var image: Image? = null

    override fun getImageSource(curve: Curve, scale: Float, description: Boolean): String {
        val pitchCurve = curve.point[0].position == 0
        val canvas = Canvas((10 + 200 * scale).toDouble(), (10 + 250 * scale).toDouble())

        with(canvas.graphicsContext2D) {
            // clear background
            fill = Color.WHITE
            fillRect(0.0, 0.0, (10 + 200 * scale).toDouble(), (10 + 250 * scale).toDouble())

            // draw out limit rect
            stroke = Color.BLACK
            strokeRect(5.0, 5.0, (200 * scale).toDouble(), (250 * scale).toDouble())

            // dashed gray lines
            lineWidth = 1.0
            lineCap = StrokeLineCap.BUTT
            lineJoin = StrokeLineJoin.ROUND
            miterLimit = 0.0
            setLineDashes(5.0, 5.0)
            stroke = Color.GRAY

            // +100% and -100% horizontal lines
            strokeLine(5.0, (5 + 25 * scale).toDouble(), (5 + 200 * scale).toDouble(), (5 + 25 * scale).toDouble())
            strokeLine(5.0, (5 + 225 * scale).toDouble(), (5 + 200 * scale).toDouble(), (5 + 225 * scale).toDouble())

            if (!pitchCurve) {
                // horizontal and vertical 0% lines
                strokeLine(5.0, (5 + 125 * scale).toDouble(), (5 + 200 * scale).toDouble(), (5 + 125 * scale).toDouble())
                strokeLine((5 + 100 * scale).toDouble(), 5.0, (5 + 100 * scale).toDouble(), (5 + 250 * scale).toDouble())
            }

            lineWidth = 1.0
            setLineDashes(0.0)
            font = Font.font("Arial", 12.0) //$NON-NLS-1$
            stroke = Color.BLACK

            val numPoints = curve.point.count { it.isEnabled }
            val xVals = DoubleArray(numPoints)
            val yVals = DoubleArray(numPoints)

            var i = 0
            for (p in curve.point)
                if (p.isEnabled) {
                    when (i) {
                        0 -> xVals[i] = (if (pitchCurve) 0 else -100).toDouble()
                        numPoints - 1 -> xVals[i] = 100.0
                        else -> xVals[i] = p.position.toDouble()
                    }
                    yVals[i] = p.value.toDouble()

                    // draw dot and point number
                    if (description) {
                        val x0: Double
                        val y0: Double
                        if (pitchCurve) {
                            x0 = 5 + xVals[i] * 2.0 * scale.toDouble()
                            y0 = 5 + (225 - yVals[i] * 2) * scale
                        } else {
                            x0 = 5 + (100 + xVals[i]) * scale
                            y0 = 5 + (125 - yVals[i]) * scale
                        }

                        strokeOval(x0 - 2, y0 - 2, 4.0, 4.0)
                        clearRect(x0 - 6, y0 - 16, 8.0, 12.0)
                        strokeText(Integer.toString(p.number + 1), x0 - 3, y0 - 5)
                    }

                    i++
                }

            lineWidth = 2.0

            if (numPoints > 2 && curve.isSmoothing) {
                // spline interpolate the curve points
                val s = SplineInterpolator()
                val function = s.interpolate(xVals, yVals)

                beginPath()

                if (pitchCurve) {
                    moveTo(5.0, 5 + (225 - yVals[0] * 2) * scale)
                    var x = 6.0
                    while (x < 4 + 200 * scale) {
                        lineTo(x, 5 + (225 - function.value((x - 5) / scale.toDouble() / 2.0) * 2) * scale)
                        x++
                    }
                } else {
                    moveTo(5.0, 5 + (125 - yVals[0]) * scale)
                    var x = 6.0
                    while (x < 4 + 200 * scale) {
                        lineTo(x, 5 + (125 - function.value((x - 5) / scale - 100)) * scale)
                        x++
                    }
                }

                stroke()
            } else {
                beginPath()

                if (pitchCurve) {
                    moveTo(5 + xVals[0] * 2.0 * scale.toDouble(), 5 + (225 - yVals[0] * 2) * scale)
                    i = 1
                    while (i < numPoints) {
                        lineTo(5 + xVals[i] * 2.0 * scale.toDouble(), 5 + (225 - yVals[i] * 2) * scale)
                        i++
                    }
                } else {
                    moveTo(5 + (100 + xVals[0]) * scale, 5 + (125 - yVals[0]) * scale)
                    i = 1
                    while (i < numPoints) {
                        lineTo(5 + (100 + xVals[i]) * scale, 5 + (125 - yVals[i]) * scale)
                        i++
                    }
                }

                stroke()
            }
        }

        val latch = CountDownLatch(1)

        // take snapshot on ui thread
        runLater {
            image = canvas.snapshot(null, null)
            latch.countDown()
        }

        // wait until snapshot completes
        latch.await()

        val renderedImage = SwingFXUtils.fromFXImage(image!!, null)

        try {
            ByteArrayOutputStream().use { baos ->
                ImageIO.write(renderedImage, "png", baos) //$NON-NLS-1$
                return CurveImageGenerator.PREFIX + Base64.getEncoder().encodeToString(baos.toByteArray())
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}
