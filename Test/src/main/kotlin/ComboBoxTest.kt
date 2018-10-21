import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Parent
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.control.Skin
import javafx.scene.control.cell.CheckBoxListCell
import javafx.scene.control.skin.ComboBoxListViewSkin
import javafx.scene.input.MouseEvent
import javafx.util.Callback
import tornadofx.*

fun main(args: Array<String>) = launch<MyApp2>(args)

class MyApp2 : App() {
    override val primaryView = MyView2::class
}

class MyView2 : View() {
    override val root: Parent = MyComboBox<String>().apply {
        items.addAll("one", "two", "three")
        prefWidth = 150.0
    }
}

class MyComboBox<T> : ComboBox<T>() {
    private val selections = mutableMapOf<T, SimpleBooleanProperty>()

    var selectedItems
        get() = selections.entries.asSequence().filter { it.value.value }.map { it.key }.toList()
        set(items) {
            selections.keys.forEach {
                if (items.contains(it)) selectItem(it) else clearSelection(it)
            }
        }

    init {
        buttonCell = ListCell<T>()
        cellFactory = Callback { _ ->
            CheckBoxListCell<T>(Callback { item ->
                // get state of item from selections or create new state
                selections.getOrPut(item) { SimpleBooleanProperty(false) }
            }).apply {
                // update button text on change
                addEventFilter(MouseEvent.MOUSE_CLICKED) {
                    buttonCell.text = selectedItems.joinToString()
                }
            }
        }
    }

    override fun createDefaultSkin(): Skin<*> = ComboBoxListViewSkin<T>(this).apply {
        // prevent ComboBox from closing on mouse click
        isHideOnClick = false
    }

    fun selectItem(item: T) {
        selections[item]?.value = true
    }

    fun clearSelection(item: T) {
        selections[item]?.value = false
    }

    fun clearAll() {
        selections.keys.forEach { clearSelection(it) }
    }

    fun selectAll() {
        selections.keys.forEach { selectItem(it) }
    }
}