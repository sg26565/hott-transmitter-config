import javafx.scene.paint.Color
import javafx.stage.StageStyle
import tornadofx.*

fun main(args: Array<String>) = launch<MyApp>(args)

class MyApp : App() {
	override val primaryView = MyView::class
}

class MyView : View() {
	override val root = vbox {
		button("Press Me") {
			action {
				openInternalWindow(MyFragment::class)
			}
		}
	}
}

class MyFragment: Fragment() {
	override val root = label("This is a popup")
}