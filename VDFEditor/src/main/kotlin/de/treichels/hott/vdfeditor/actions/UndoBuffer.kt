package de.treichels.hott.vdfeditor.actions

import javafx.beans.property.ReadOnlyBooleanProperty
import tornadofx.*

class UndoBuffer<T>(private val items: MutableList<T>) {
    // current action pointer
    internal var index: Int = 0
        private set

    // list of actions managed by this undo buffer
    internal val actions = mutableListOf<UndoableAction<T>>()

    var canRedo: Boolean by property(false)
        private set

    var canUndo: Boolean by property(false)
        private set

    // JavaFX properties for binding to UI elements
    fun canRedoProperty(): ReadOnlyBooleanProperty = ReadOnlyBooleanProperty.readOnlyBooleanProperty(getProperty(UndoBuffer<T>::canRedo))

    fun canUndoProperty(): ReadOnlyBooleanProperty = ReadOnlyBooleanProperty.readOnlyBooleanProperty(getProperty(UndoBuffer<T>::canUndo))

    // the current action
    private val currentAction
        get() = actions[index]

    /**
     * Reset this undo buffer and remove all stored actions.
     */
    fun clear() {
        actions.clear()
        index = 0
        canRedo = false
        canUndo = false
    }

    /**
     * Undo current action and move action pointer accordingly
     */
    fun undo() {
        if (index > 0) {
            index--
            currentAction.undo(items)
            canRedo = true
            canUndo = index > 0
        }
    }

    /**
     * Redo the current action and move action pointer accordingly
     */
    fun redo() {
        if (index < actions.size) {
            currentAction.apply {
                index++
                apply(items)
            }

            canRedo = index < actions.size
            canUndo = true
        }
    }

    /**
     * Push a new action on top of the undo buffer. The action will be applied and can be undone afterwards.
     * This will also withdraw all undone actions (i.e. they can not be redone)
     */
    fun push(action: UndoableAction<T>) {
        // withdraw any actions that were undone previously, but not redone
        while (index < actions.size)
            actions.removeAt(index)

        actions.add(action)
        redo()
    }

    /**
     * Undo the current action and remove is from the undo buffer. Only allowed if there are no undone actions.
     */
    fun pop() {
        if (index != actions.size) throw IllegalArgumentException()

        undo()
        actions.removeAt(index)
        canRedo = false
    }
}
