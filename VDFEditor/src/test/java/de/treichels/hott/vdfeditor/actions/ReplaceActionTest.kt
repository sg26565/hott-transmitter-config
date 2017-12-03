package de.treichels.hott.vdfeditor.actions

import org.junit.Test
import kotlin.test.assertEquals

class ReplaceActionTest : AbstractActionTest() {
    @Test
    fun testReplaceAction() {
        val action = ReplaceAction(2, "x")

        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[a, b, x, d]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }
}
