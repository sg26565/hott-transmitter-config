package de.treichels.hott.vdfeditor.actions

import org.junit.Test
import kotlin.test.assertEquals

class AddActionTest : AbstractActionTest() {
    @Test
    fun testAddAction() {
        val action = AddAction("x")
        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[a, b, c, d, x]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }
}
