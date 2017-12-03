package de.treichels.hott.vdfeditor.actions

import org.junit.Test
import kotlin.test.assertEquals

class MoveDownActionTest : AbstractActionTest() {
    @Test
    fun testMoveDownAction1() {
        val action = MoveDownAction<String>(0)
        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[b, a, c, d]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }

    @Test
    fun testMoveDownAction2() {
        val action = MoveDownAction<String>(2)
        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[a, b, d, c]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }
}
