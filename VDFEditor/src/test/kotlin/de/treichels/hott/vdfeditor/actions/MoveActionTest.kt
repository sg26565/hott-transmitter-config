package de.treichels.hott.vdfeditor.actions

import org.junit.Test
import kotlin.test.assertEquals

class MoveActionTest : AbstractActionTest() {
    @Test
    fun testMoveAction1() {
        val action = MoveAction<String>(2, 0)

        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[c, a, b, d]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }

    @Test
    fun testMoveAction2() {
        val action = MoveAction<String>(3, 2)

        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[a, b, d, c]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }

    @Test
    fun testMoveAction3() {
        val action = MoveAction<String>(2, 3)

        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[a, b, d, c]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }

    @Test
    fun testMoveAction4() {
        val action = MoveAction<String>(0, 2)

        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[b, c, a, d]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }
}
