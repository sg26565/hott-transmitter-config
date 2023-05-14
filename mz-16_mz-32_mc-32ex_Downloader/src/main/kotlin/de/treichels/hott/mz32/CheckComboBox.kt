package de.treichels.hott.mz32

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.control.cell.CheckBoxListCell
import javafx.scene.control.skin.ComboBoxListViewSkin
import javafx.util.Callback
import tornadofx.*

/**
 * A combo box that displays a list of check boxes which can be selected individually.
 */
class CheckComboBox<T> : ComboBox<T>() {
    /** all items of the combo box along with a BooleanProperty that can be observed to render the check boxes. */
    private val selections = mutableMapOf<T, SimpleBooleanProperty>()

    /** an ObservableList containing all selected items. */
    val selectedItems: ObservableList<T> = mutableListOf<T>().asObservable()

    init {
        // disable standard rendering of combo box value
        buttonCell = ListCell<T>()

        // re-use default skin
        skin = ComboBoxListViewSkin(this).apply { isHideOnClick = false }

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
