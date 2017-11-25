package de.treichels.hott.vdfeditor.actions

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UndoBufferTest {
    private val items = mutableListOf<String>()
    private var buffer = UndoBuffer(items)

    @Before
    fun setup() {
        items.clear()
        buffer.clear()
    }

    @Test
    fun testClear() {
        testPushAdd()

        buffer.clear()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(0, buffer.index.toLong())
        assertEquals(0, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertFalse(buffer.canUndo)
        assertFalse(buffer.canUndoProperty().get())
    }

    @Test
    fun testEmpty() {
        assertEquals("[]", items.toString())
        assertEquals(0, buffer.index.toLong())
        assertEquals(0, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertFalse(buffer.canUndo)
        assertFalse(buffer.canUndoProperty().get())
    }

    @Test
    fun testEmptyRedo() {
        testEmpty()

        buffer.redo()
        testEmpty()
    }

    @Test
    fun testEmptyRedo2() {
        testRedoAdd()

        buffer.redo()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(3, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testEmptyUndo() {
        testEmpty()

        buffer.undo()
        testEmpty()
    }

    @Test
    fun testEmptyUndo2() {
        testUndoAdd()

        buffer.undo()
        assertEquals("[]", items.toString())
        assertEquals(0, buffer.index.toLong())
        assertEquals(3, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertFalse(buffer.canUndo)
        assertFalse(buffer.canUndoProperty().get())
    }

    @Test
    fun testPop() {
        testPushAdd()

        buffer.pop()
        assertEquals("[a, b]", items.toString())
        assertEquals(2, buffer.index.toLong())
        assertEquals(2, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.pop()
        assertEquals("[a]", items.toString())
        assertEquals(1, buffer.index.toLong())
        assertEquals(1, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.pop()
        testEmpty()
    }

    @Test
    fun testPushAdd() {
        testEmpty()

        buffer.push(AddAction("a"))
        assertEquals("[a]", items.toString())
        assertEquals(1, buffer.index.toLong())
        assertEquals(1, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.push(AddAction("b"))
        assertEquals("[a, b]", items.toString())
        assertEquals(2, buffer.index.toLong())
        assertEquals(2, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.push(AddAction("c"))
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(3, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testPushAfterUndo() {
        testPushAdd()

        buffer.undo()
        assertEquals("[a, b]", items.toString())
        assertEquals(2, buffer.index.toLong())
        assertEquals(3, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.push(AddAction("d"))
        assertEquals("[a, b, d]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(3, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.undo()
        assertEquals("[a, b]", items.toString())
        assertEquals(2, buffer.index.toLong())
        assertEquals(3, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.undo()
        assertEquals("[a]", items.toString())
        assertEquals(1, buffer.index.toLong())
        assertEquals(3, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.push(AddAction("e"))
        assertEquals("[a, e]", items.toString())
        assertEquals(2, buffer.index.toLong())
        assertEquals(2, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testPushInsert() {
        testPushAdd()

        buffer.push(InsertAction(1, "x"))
        assertEquals("[a, x, b, c]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.undo()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.redo()
        assertEquals("[a, x, b, c]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testPushMove() {
        testPushAdd()

        buffer.push(MoveAction(1, 2))
        assertEquals("[a, c, b]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.undo()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.redo()
        assertEquals("[a, c, b]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testPushMoveDown() {
        testPushAdd()

        buffer.push(MoveDownAction(1))
        assertEquals("[a, c, b]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.undo()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.redo()
        assertEquals("[a, c, b]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testPushMoveUp() {
        testPushAdd()

        buffer.push(MoveUpAction(2))
        assertEquals("[a, c, b]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.undo()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.redo()
        assertEquals("[a, c, b]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testPushRemove() {
        testPushAdd()

        buffer.push(RemoveAction(1))
        assertEquals("[a, c]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.undo()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.redo()
        assertEquals("[a, c]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testPushReplace() {
        testPushAdd()

        buffer.push(ReplaceAction(1, "x"))
        assertEquals("[a, x, c]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.undo()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.redo()
        assertEquals("[a, x, c]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testRedoAdd() {
        testUndoAdd()

        buffer.redo()
        assertEquals("[a]", items.toString())
        assertEquals(1, buffer.index.toLong())
        assertEquals(3, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.redo()
        assertEquals("[a, b]", items.toString())
        assertEquals(2, buffer.index.toLong())
        assertEquals(3, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.redo()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(3, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testRedoMove() {
        testUndoMove()

        buffer.redo()
        assertEquals("[a, c, b]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testRedoMoveDown() {
        testUndoMove()

        buffer.redo()
        assertEquals("[a, c, b]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testRedoMoveUp() {
        testUndoMove()

        buffer.redo()
        assertEquals("[a, c, b]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testRedoRemove() {
        testUndoRemove()

        buffer.redo()
        assertEquals("[a, c]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testRedoReplace() {
        testUndoReplace()

        buffer.redo()
        assertEquals("[a, x, c]", items.toString())
        assertEquals(4, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertFalse(buffer.canRedo)
        assertFalse(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testUndoAdd() {
        testPushAdd()

        buffer.undo()
        assertEquals("[a, b]", items.toString())
        assertEquals(2, buffer.index.toLong())
        assertEquals(3, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.undo()
        assertEquals("[a]", items.toString())
        assertEquals(1, buffer.index.toLong())
        assertEquals(3, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())

        buffer.undo()
        assertEquals("[]", items.toString())
        assertEquals(0, buffer.index.toLong())
        assertEquals(3, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertFalse(buffer.canUndo)
        assertFalse(buffer.canUndoProperty().get())
    }

    @Test
    fun testUndoMove() {
        testPushMove()

        buffer.undo()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testUndoMoveDown() {
        testPushMoveDown()

        buffer.undo()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testUndoMoveUp() {
        testPushMoveUp()

        buffer.undo()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testUndoRemove() {
        testPushRemove()

        buffer.undo()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }

    @Test
    fun testUndoReplace() {
        testPushReplace()

        buffer.undo()
        assertEquals("[a, b, c]", items.toString())
        assertEquals(3, buffer.index.toLong())
        assertEquals(4, buffer.actions.size.toLong())
        assertTrue(buffer.canRedo)
        assertTrue(buffer.canRedoProperty().get())
        assertTrue(buffer.canUndo)
        assertTrue(buffer.canUndoProperty().get())
    }
}
