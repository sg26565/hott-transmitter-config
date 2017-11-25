package de.treichels.hott.vdfeditor.actions

import org.junit.Test
import kotlin.test.assertEquals

class MoveUpActionTest : AbstractActionTest() {
    @Test
    fun testMoveUpAction1() {
        val action = MoveUpAction<String>(1)
        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[b, a, c, d]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }

    @Test
    fun testMoveUpAction2() {
        val action = MoveUpAction<String>(3)
        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[a, b, d, c]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }
}
