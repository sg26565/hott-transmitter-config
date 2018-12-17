package de.treichels.hott.mz32

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.control.Skin
import javafx.scene.control.cell.CheckBoxListCell
import javafx.util.Callback
import tornadofx.*

/**
 * A combo box that displays a list of check boxes which can be selected individually.
 */
class CheckComboBox<T> : ComboBox<T>() {
    /** all items of the combo box along with a BooleanProperty that can be observed to render the check boxes. */
    private val selections = mutableMapOf<T, SimpleBooleanProperty>()

    /** an ObservableList containing all selected items. */
    val selectedItems: ObservableList<T> = mutableListOf<T>().observable()

    init {
        // disable standard rendering of combo box value
        buttonCell = ListCell<T>()

        // Java version quirks - load skin class via reflection
        // The Java 8 class inherits from a parent class that does not exist in later versions
        // The Java 10 class does not exist in Java 8
        val isJava8 = System.getProperty("java.version").startsWith("1.8")
        val skinClass = Class.forName(if (isJava8) "de.treichels.hott.Java8ComboBoxListViewSkin" else "javafx.scene.control.skin.ComboBoxListViewSkin")
        val constructor = skinClass.getConstructor(ComboBox::class.java)

        // re-use default skin
        skin = constructor.newInstance(this) as Skin<*>?

        // disable auto close
        if (isJava8) {
            // overwritten method skin.isHideOnClickEnabled() returns false on Java 8
        } else {
            // call skin.setHideOnClick(false) through reflection
            val method = skinClass.getMethod("setHideOnClick", Boolean::class.java)
            method.invoke(skin, false)
        }

        // custom cell factory
        cellFactory = Callback { CheckBoxListCell<T>(Callback(::getSelection)) }

        // render combo box value
        selectedItems.onChange { updateText() }
        runLater { updateText() }
    }

    /** update button text on change */
    private fun updateText() {
        buttonCell.text = selectedItems.sorted().joinToString()
    }

    /**
     * Manage selections
     *
     * Gets the observable state of an item from the internal map or adds it if it does not already exist. A change
     * listener will maintain the selectedItems list according to the state of the check box.
     */
    private fun getSelection(item: T) = selections.getOrPut(item) {
        SimpleBooleanProperty(false).apply {
            // add a change listener to update selected values
            addListener { _, _, newValue ->
                if (newValue && !selectedItems.contains(item)) selectedItems.add(item)
                if (!newValue && selectedItems.contains(item)) selectedItems.remove(item)
            }
        }
    }

    /** Select (check) and item in the combo box. */
    fun selectItem(item: T) {
        if (items.contains(item)) getSelection(item).value = true
    }
}