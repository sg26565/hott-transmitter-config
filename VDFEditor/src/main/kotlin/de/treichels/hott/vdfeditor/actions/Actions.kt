package de.treichels.hott.vdfeditor.actions

import de.treichels.hott.voice.VoiceData

/**
 * Abstract base class for undoable actions for mutable lists of type <T>.
 */
abstract class UndoableAction<T> {
    abstract fun apply(list: MutableList<T>): MutableList<T>
    abstract fun undo(list: MutableList<T>): MutableList<T>
}

/**
 * Insert a new item into the list.
 */
class InsertAction<T>(private val index: Int, private val item: T) : UndoableAction<T>() {
    override fun apply(list: MutableList<T>): MutableList<T> {
        list.add(index, item)
        return list
    }

    override fun undo(list: MutableList<T>): MutableList<T> {
        list.removeAt(index)
        return list
    }
}

/**
 * Add a new item to the end of the list.
 */
class AddAction<T>(private val item: T) : UndoableAction<T>() {
    private var index: Int = -1

    override fun apply(list: MutableList<T>): MutableList<T> {
        index = list.size
        list.add(index, item)
        return list
    }

    override fun undo(list: MutableList<T>): MutableList<T> {
        list.removeAt(index)
        return list
    }
}

/**
 * Remove an item from the list.
 */
class RemoveAction<T>(private val index: Int) : UndoableAction<T>() {
    private var item: T? = null

    override fun apply(list: MutableList<T>): MutableList<T> {
        item = list.removeAt(index)
        return list
    }

    override fun undo(list: MutableList<T>): MutableList<T> {
        list.add(index, item!!)
        return list
    }
}

/**
 * Replace an existing item with a new one.
 */
class ReplaceAction<T>(private val index: Int, private val item: T) : UndoableAction<T>() {
    private var oldItem: T? = null

    override fun apply(list: MutableList<T>): MutableList<T> {
        oldItem = list[index]
        list[index] = item
        return list
    }

    override fun undo(list: MutableList<T>): MutableList<T> {
        list[index] = oldItem!!
        return list
    }
}

/**
 * Move an item in the list.
 */
open class MoveAction<T>(private val fromIndex: Int, private val toIndex: Int) : UndoableAction<T>() {
    override fun apply(list: MutableList<T>): MutableList<T> {
        val item = list.removeAt(fromIndex)
        list.add(toIndex, item)
        return list
    }

    override fun undo(list: MutableList<T>): MutableList<T> {
        val item = list.removeAt(toIndex)
        list.add(fromIndex, item)
        return list
    }
}

class MoveDownAction<T>(index: Int) : MoveAction<T>(fromIndex = index, toIndex = index + 1)
class MoveUpAction<T>(index: Int) : MoveAction<T>(fromIndex = index, toIndex = index - 1)

class RenameAction(private val index: Int, private val newName: String) : UndoableAction<VoiceData>() {
    private lateinit var oldName: String

    override fun apply(list: MutableList<VoiceData>): MutableList<VoiceData> {
        val voiceData = list[index]
        oldName = voiceData.name
        voiceData.name = newName
        list[index] = voiceData
        return list
    }

    override fun undo(list: MutableList<VoiceData>): MutableList<VoiceData> {
        val voiceData = list[index]
        voiceData.name = oldName
        list[index] = voiceData
        return list
    }
}