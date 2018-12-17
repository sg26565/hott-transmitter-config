package de.treichels.hott.vdfeditor.actions

import org.junit.Test
import kotlin.test.assertEquals

class InsertActionTest : AbstractActionTest() {
    @Test
    fun testInsertAction1() {
        val action = InsertAction(2, "x")

        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[a, b, x, c, d]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }

    @Test
    fun testInsertAction2() {
        val action = InsertAction(0, "x")

        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[x, a, b, c, d]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }

    @Test
    fun testInsertAction3() {
        val action = InsertAction(4, "x")

        assertEquals("[a, b, c, d]", list.toString())
        action.apply(list)
        assertEquals("[a, b, c, d, x]", list.toString())
        action.undo(list)
        assertEquals("[a, b, c, d]", list.toString())
    }
}
