package de.treichels.hott.vdfeditor.actions

import javafx.beans.property.SimpleBooleanProperty
import tornadofx.*

class UndoBuffer<T>(private val items: MutableList<T>) {
    // JavaFX properties for binding to UI elements
    val canRedoProperty = SimpleBooleanProperty(false)
    val canUndoProperty = SimpleBooleanProperty(false)

    // current action pointer
    var index: Int = 0
        private set

    // list of actions managed by this undo buffer
    val actions = mutableListOf<UndoableAction<T>>()

    // private helpers
    private var canRedo by canRedoProperty
    private var canUndo by canUndoProperty

    // the current action
    private val currentAction
        get() = actions[index]

    /**
     * Reset this undo buffer and remove all stored actions.
     */
    fun clear() {
        actions.clear()
        index = 0
        canRedo = false;
        canUndo = false;
    }

    /**
     * Undo current action and move action pointer accordingly
     */
    fun undo() {
        index--
        currentAction.undo(items);
        canUndo = index > 0
        canRedo = true
    }

    /**
     * Redo the current action and move action pointer accordingly
     */
    fun redo() {
        currentAction.apply(items)
        index++
        canRedo = index < actions.size
        canUndo = true
    }

    /**
     * Push a new action on top of the undo buffer. The action will be applied and can be undone afterwards.
     * This will also withdraw all undone actions (i.e. they can not be redone)
     */
    fun push(action: UndoableAction<T>) {
        while (index < actions.size)
            actions.removeAt(index)

        actions.add(action)
        redo()
        canUndo = true
    }

    /**
     * Undo the current action and remove is from the undo buffer. Only allowed if there are no undone actions.
     */
    fun pop() {
        if (index != actions.size) throw IllegalArgumentException()

        undo()
        actions.removeAt(index);
        canRedo = false
    }
}
