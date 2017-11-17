import javafx.scene.control.Label
import javafx.scene.layout.VBox
import tornadofx.App
import tornadofx.View
import tornadofx.launch

fun main(args: Array<String>) = launch<MyApp>(args)

class MyApp : App() {
	override val primaryView = MyView::class
}

class MyView : View() {
	override val root = VBox()
	
	init {
		root.children += Label("Hello, Wolrd!")
	}
}