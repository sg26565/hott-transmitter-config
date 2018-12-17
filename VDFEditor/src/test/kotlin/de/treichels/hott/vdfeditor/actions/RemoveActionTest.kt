package de.treichels.hott.vdfeditor.actions

import org.junit.Test
import kotlin.test.assertEquals

class RemoveActionTest : AbstractActionTest() {
    @Test
    fun testRemoveAction1() {
        val action = RemoveAction<String>(2)
        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[a, b, d]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }

    @Test
    fun testRemoveAction2() {
        val action = RemoveAction<String>(0)
        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[b, c, d]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }

    @Test
    fun testRemoveAction3() {
        val action = RemoveAction<String>(3)
        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[a, b, c]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }
}
