import javafx.scene.paint.Color
import tornadofx.*

fun main(args: Array<String>) = launch<MyApp>(args)

class MyApp : App() {
	override val primaryView = MyView::class
}

class MyView : View() {
	override val root = vbox {
		val button = button {
			text = "Press me!"
		}
		val label = label {
			text = "not pressed"
		}

		button.action {
			label.text = "pressed"
		}
	}
}