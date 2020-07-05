import de.treichels.hott.model.Curve
import de.treichels.hott.model.CurvePoint
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import javafx.scene.canvas.Canvas
import javafx.scene.control.Slider
import javafx.scene.image.ImageView
import javafx.scene.paint.Color
import javafx.scene.shape.StrokeLineCap
import javafx.scene.shape.StrokeLineJoin
import javafx.scene.text.Font
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator
import tornadofx.*

fun main(args: Array<String>) = launch<CurveTest>(args)

class CurveTest : App() {
    override val primaryView = CurveTestView::class
}

class CurveTestView : View("Curve Test") {
    private var imageView by singleAssign<ImageView>()
    private var scaleSlider by singleAssign<Slider>()
    private var drSlider by singleAssign<Slider>()
    private var xSlider by singleAssign<Slider>()

    override val root = borderpane {
        center {
            imageView = imageview()
        }

        bottom {
            hbox {
                alignment = Pos.CENTER

                hbox {
                    alignment = Pos.CENTER
                    paddingAll = 5

                    label("Scale")
                    scaleSlider = slider(.5, 4, 1) {
                        isShowTickLabels = true
                        isShowTickMarks = true
                        majorTickUnit = 0.5
                        minorTickCount = 5
                        blockIncrement = 0.1
                    }
                }

                hbox {
                    alignment = Pos.CENTER
                    paddingAll = 5

                    label("Dual Rate")
                    drSlider = slider(0, 100, 100) {
                        isShowTickLabels = true
                        isShowTickMarks = true
                        majorTickUnit = 25.0
                        minorTickCount = 5
                        blockIncrement = 5.0
                    }
                }

                hbox {
                    alignment = Pos.CENTER
                    paddingAll = 5

                    label("X")
                    xSlider = slider(-100, 100, 0) {
                        isShowTickLabels = true
                        isShowTickMarks = true
                        majorTickUnit = 50.0
                        minorTickCount = 5
                        blockIncrement = 5.0
                    }
                    label {
                        val x = SimpleIntegerProperty()
                        x.bind(xSlider.valueProperty())
                        textProperty().bind(x.asString())
                    }
                }
            }
        }
    }

    init {
        scaleSlider.valueProperty().addListener { _ ->
            getImageSource()
        }
        drSlider.valueProperty().addListener { _ ->
            getImageSource()
        }
        xSlider.valueProperty().addListener { _ ->
            getImageSource()
        }

        runLater {
            getImageSource()
        }
    }

    fun getImageSource() {
        val scale = scaleSlider.value
        val dr = drSlider.value.toInt()
        val x = 50 + xSlider.value.toInt() * 28 / 100
        val y = 50

        val curve = Curve(point = listOf(
                CurvePoint(number = 0, isEnabled = true, position = -100, value = -dr),
                CurvePoint(number = 1, isEnabled = true, position = -x, value = -y * dr / 100),
                CurvePoint(number = 2, isEnabled = true, position = 0, value = 0),
                CurvePoint(number = 3, isEnabled = true, position = x, value = y * dr / 100),
                CurvePoint(number = 4, isEnabled = true, position = 100, value = dr)
        ), isSmoothing = true)

        val pitchCurve = curve.point[0].position == 0
        val canvas = Canvas((10 + 200 * scale), (10 + 250 * scale))

        with(canvas.graphicsContext2D) {
            // clear background
            fill = Color.WHITE
            fillRect(0.0, 0.0, (10 + 200 * scale), (10 + 250 * scale))

            // draw out limit rect
            stroke = Color.BLACK
            strokeRect(5.0, 5.0, (200 * scale), (250 * scale))

            // dashed gray lines
            lineWidth = 1.0
            lineCap = StrokeLineCap.BUTT
            lineJoin = StrokeLineJoin.ROUND
            miterLimit = 0.0
            setLineDashes(5.0, 5.0)
            stroke = Color.GRAY

            // +100% and -100% horizontal lines
            strokeLine(5.0, (5 + 25 * scale), (5 + 200 * scale), (5 + 25 * scale))
            strokeLine(5.0, (5 + 225 * scale), (5 + 200 * scale), (5 + 225 * scale))

            if (!pitchCurve) {
                // horizontal and vertical 0% lines
                strokeLine(5.0, (5 + 125 * scale), (5 + 200 * scale), (5 + 125 * scale))
                strokeLine((5 + 100 * scale), 5.0, (5 + 100 * scale), (5 + 250 * scale))
            }

            lineWidth = 1.0
            setLineDashes(0.0)
            font = Font.font("Arial", 12.0)
            stroke = Color.BLACK

            val numPoints = curve.point.count { it.isEnabled }
            val xVals = DoubleArray(numPoints)
            val yVals = DoubleArray(numPoints)

            curve.point.filter { it.isEnabled }.forEachIndexed { i, p ->
                when (i) {
                    0 -> xVals[i] = if (pitchCurve) 0.0 else -100.0
                    numPoints - 1 -> xVals[i] = 100.0
                    else -> xVals[i] = p.position.toDouble()
                }
                yVals[i] = p.value.toDouble()

                // draw dot and point number
                val x0: Double
                val y0: Double
                if (pitchCurve) {
                    x0 = 5 + xVals[i] * 2.0 * scale
                    y0 = 5 + (225 - yVals[i] * 2) * scale
                } else {
                    x0 = 5 + (100 + xVals[i]) * scale
                    y0 = 5 + (125 - yVals[i]) * scale
                }

                strokeOval(x0 - 2, y0 - 2, 4.0, 4.0)
                clearRect(x0 - 6, y0 - 16, 8.0, 12.0)
                strokeText(Integer.toString(p.number + 1), x0 - 3, y0 - 5)
            }

            lineWidth = 2.0

            if (numPoints > 2 && curve.isSmoothing) {
                // spline interpolate the curve points
                val s = SplineInterpolator()
                val function = s.interpolate(xVals, yVals)

                beginPath()

                if (pitchCurve) {
                    moveTo(5.0, 5 + (225 - yVals[0] * 2) * scale)
                    var x1 = 6.0
                    while (x1 < 4 + 200 * scale) {
                        lineTo(x1, 5 + (225 - function.value((x1 - 5) / scale / 2.0) * 2) * scale)
                        x1++
                    }
                } else {
                    moveTo(5.0, 5 + (125 - yVals[0]) * scale)
                    var x1 = 6.0
                    while (x1 < 4 + 200 * scale) {
                        lineTo(x1, 5 + (125 - function.value((x1 - 5) / scale - 100)) * scale)
                        x1++
                    }
                }

                stroke()
            } else {
                beginPath()

                if (pitchCurve) {
                    moveTo(5 + xVals[0] * 2.0 * scale, 5 + (225 - yVals[0] * 2) * scale)
                    for (i in 1 until numPoints) {
                        lineTo(5 + xVals[i] * 2.0 * scale, 5 + (225 - yVals[i] * 2) * scale)
                    }
                } else {
                    moveTo(5 + (100 + xVals[0]) * scale, 5 + (125 - yVals[0]) * scale)
                    for (i in 1 until numPoints) {
                        lineTo(5 + (100 + xVals[i]) * scale, 5 + (125 - yVals[i]) * scale)
                    }
                }

                stroke()
            }
        }

        imageView.image = canvas.snapshot(null, null)
    }
}
